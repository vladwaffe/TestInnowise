package com.connectdepartmentandworkerservice.services;

import com.connectdepartmentandworkerservice.hibernate.HibernateUtils;
import com.connectdepartmentandworkerservice.model.WorkerDepartment;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConnectService {

    private static final Logger logger = LoggerFactory.getLogger(ConnectService.class);

    public List<Long> loadDepartmentIdsForWorker(Long workerId) {
        Session session = HibernateUtils.startSession();
        try {
            String hql = "FROM WorkerDepartment WHERE workerId = :workerId";
            Query<WorkerDepartment> query = session.createQuery(hql, WorkerDepartment.class);
            query.setParameter("workerId", workerId);
            List<WorkerDepartment> workerDepartments = query.getResultList();

            return workerDepartments.stream()
                    .map(WorkerDepartment::getDepartmentId)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error in loadDepartmentIdsForWorker: ", e);
            return new ArrayList<>();
        } finally {
            if (session != null) {
                HibernateUtils.closeSession();
            }
        }
    }

    public void createWorkerDepartmentAssociation(Long workerId, Long departmentId) {
        Session session = HibernateUtils.startSession();
        try {
            WorkerDepartment workerDepartment = new WorkerDepartment();
            workerDepartment.setWorkerId(workerId);
            workerDepartment.setDepartmentId(departmentId);

            session.beginTransaction();
            session.save(workerDepartment);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Error in createWorkerDepartmentAssociation: ", e);
        } finally {
            if (session != null) {
                HibernateUtils.closeSession();
            }
        }
    }

    public void softDeleteWorkerDepartmentAssociation(Long workerId, Long departmentId) {
        Session session = HibernateUtils.startSession();
        try {
            String hql = "UPDATE WorkerDepartment SET isDeleted = true WHERE workerId = :workerId AND departmentId = :departmentId";
            Query query = session.createQuery(hql);
            query.setParameter("workerId", workerId);
            query.setParameter("departmentId", departmentId);

            session.beginTransaction();
            query.executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Error in softDeleteWorkerDepartmentAssociation: ", e);
        } finally {
            if (session != null) {
                HibernateUtils.closeSession();
            }
        }
    }

    public void updateWorkerDepartmentAssociations(Long workerId, List<Long> newDepartmentIds) {
        Session session = HibernateUtils.startSession();
        try {
            String deleteHql = "DELETE FROM WorkerDepartment WHERE workerId = :workerId";
            Query deleteQuery = session.createQuery(deleteHql);
            deleteQuery.setParameter("workerId", workerId);
            session.beginTransaction();
            deleteQuery.executeUpdate();
            session.getTransaction().commit();

            session.beginTransaction();
            for (Long departmentId : newDepartmentIds) {
                WorkerDepartment workerDepartment = new WorkerDepartment();
                workerDepartment.setWorkerId(workerId);
                workerDepartment.setDepartmentId(departmentId);
                session.save(workerDepartment);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Error in updateWorkerDepartmentAssociations: ", e);
        } finally {
            if (session != null) {
                HibernateUtils.closeSession();
            }
        }
    }


    public List<Long> getWorkerIdsByDepartmentIds(List<Long> departmentIds) {
        Session session = HibernateUtils.startSession();
        try {
            String hql = "SELECT DISTINCT workerId FROM WorkerDepartment WHERE departmentId IN :departmentIds AND isDeleted = false";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("departmentIds", departmentIds);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error in getWorkerIdsByDepartmentIds: ", e);
            return new ArrayList<>();
        } finally {
            if (session != null) {
                HibernateUtils.closeSession();
            }
        }
    }
}
