package com.workerservice.services;

import com.workerservice.exceptions.WorkerAlreadyExistException;
import com.workerservice.exceptions.WorkerNotFoundException;
import com.workerservice.hibernate.HibernateUtils;
import com.workerservice.model.DTO.AnswerWorkerDTO;
import com.workerservice.model.DTO.WorkerDTO;
import com.workerservice.model.Worker;
import com.workerservice.model.mapper.WorkerMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WorkerService {
    @Autowired
    private RestTemplate restTemplate;

    private static final String DEPARTMENT_CONNECT_SERVICE_URL = "http://department-connect-service:8081/department/";
    private static final String DEPARTMENT_SERVICE_URL = "http://department-service:8082/departments/";
    private static final String IMAGE_SERVICE_URL = "http://image-service:8083/image/";
    private static final Logger logger = LoggerFactory.getLogger(WorkerService.class);

    private List<AnswerWorkerDTO> getWorkersData(List<Worker> workerList){
        List<AnswerWorkerDTO> answerWorkerDTOS = new ArrayList<>();
        for (Worker worker : workerList) {
            answerWorkerDTOS.add(getWorkerData(worker));
        }
        return answerWorkerDTOS;
    }
    private AnswerWorkerDTO getWorkerData(Worker worker){
        AnswerWorkerDTO answerWorkerDTO = WorkerMapper.INSTANCE.workerToAnswerWorkerDTO(worker);
        String departmentUrl = DEPARTMENT_CONNECT_SERVICE_URL + worker.getId();
        List<Integer> departmentIdList = restTemplate.getForObject(departmentUrl, List.class);
        List<String> stringList = new ArrayList<>();
        for(Integer id : departmentIdList){
            String getDepartUrl = DEPARTMENT_SERVICE_URL + id;
            Map<String, Object> response = restTemplate.getForObject(getDepartUrl, Map.class);
            if (response != null && response.get("name") != null) {
                stringList.add(response.get("name").toString());
            }
        }
        String getImageUrl = IMAGE_SERVICE_URL + worker.getId();
        answerWorkerDTO.setDepartmentNames(stringList);
        answerWorkerDTO.setImage(restTemplate.getForObject(getImageUrl, byte[].class));
        return answerWorkerDTO;
    }
    private void saveWorkerDepartments(Long workerId, List<Long> departmentIds) {
        try {
            for (Long departmentId : departmentIds) {
                String url = DEPARTMENT_CONNECT_SERVICE_URL + workerId + "/" + departmentId;
                restTemplate.postForObject(url, null, Void.class);
            }
        } catch (Exception e) {
            logger.error("Error in saveWorkerDepartments: ", e);
        }
    }

    public void deleteById(Long id) {
        Session session = null;
        try {
            session = HibernateUtils.startSession();
            Worker worker = session.get(Worker.class, id);
            if (worker == null) {
                throw new WorkerNotFoundException("Работник не найден: " + id);
            } else {
                session.beginTransaction();
                worker.setDeleted(true);
                session.update(worker);
                session.getTransaction().commit();
            }
        } catch (Exception e) {
            logger.error("Error in deleteById: ", e);
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                HibernateUtils.closeSession();
            }
        }
    }

    public AnswerWorkerDTO findById(Long id) {
        try {
            Worker worker = HibernateUtils.startSession().get(Worker.class, id);
            HibernateUtils.closeSession();
            if (worker == null) {
                return null;
            } else {
                return getWorkerData(worker);
            }
        } catch (Exception e) {
            logger.error("Error in findById: ", e);
            return null;
        }
    }

    public WorkerDTO saveWorker(WorkerDTO workerDTO) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtils.startSession();
            transaction = session.beginTransaction();

            Worker existingWorker = session.createQuery(
                            "FROM Worker WHERE firstname = :firstname AND lastname = :lastname", Worker.class)
                    .setParameter("firstname", workerDTO.getFirstname())
                    .setParameter("lastname", workerDTO.getLastname())
                    .uniqueResult();

            if (existingWorker != null) {
                if (existingWorker.isDeleted()) {
                    existingWorker.setDeleted(false);
                    session.update(existingWorker);
                } else {
                    workerDTO = null;
                    throw new WorkerAlreadyExistException("Worker already exists", HttpStatus.CONFLICT);
                }
            } else {
                Worker worker = WorkerMapper.INSTANCE.workerDTOToWorker(workerDTO);
                session.persist(worker);
                transaction.commit();
                saveWorkerDepartments(worker.getId(), workerDTO.getDepartmentIds());
            }

        } catch (Exception e) {
            logger.error("Error in saveWorker: ", e);
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                HibernateUtils.closeSession();
            }
        }
        return workerDTO;
    }







    public List<AnswerWorkerDTO> findAllWithFilters(String name, List<Long> departmentIds, String status, String sortField, boolean ascending, int page, int size) {
        Session session = null;
        try {
            session = HibernateUtils.startSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Worker> cq = cb.createQuery(Worker.class);
            Root<Worker> root = cq.from(Worker.class);

            List<Predicate> predicates = new ArrayList<>();

            if (name != null) {
                predicates.add(cb.equal(root.get("firstname"), name));
            }


            if (departmentIds != null && !departmentIds.isEmpty()) {
                String url = DEPARTMENT_CONNECT_SERVICE_URL + "workers?departmentIds=" + departmentIds.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(","));
                System.out.println(restTemplate.getForObject(url, List.class));
                List<Long> workerIds = restTemplate.getForObject(url, List.class);

                if (workerIds != null && !workerIds.isEmpty()) {
                    predicates.add(root.get("id").in(workerIds));
                } else {
                    return new ArrayList<>();
                }
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            predicates.add(cb.isFalse(root.get("deleted")));

            cq.where(predicates.toArray(new Predicate[0]));

            if (sortField != null) {
                if (ascending) {
                    cq.orderBy(cb.asc(root.get(sortField)));
                } else {
                    cq.orderBy(cb.desc(root.get(sortField)));
                }
            }

            Query<Worker> query = session.createQuery(cq);
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);

            List<Worker> workers = query.getResultList();

            return getWorkersData(workers);
        } catch (Exception e) {
            logger.error("Error in findAllWithFilters: ", e);
            return new ArrayList<>();
        } finally {
            if (session != null) {
                HibernateUtils.closeSession();
            }
        }
    }


    public WorkerDTO updateWorker(WorkerDTO workerDTO) {
        Session session = HibernateUtils.startSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.saveOrUpdate(WorkerMapper.INSTANCE.workerDTOToWorker(workerDTO));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            HibernateUtils.closeSession();
        }
        return workerDTO;
    }
}
