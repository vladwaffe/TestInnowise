package com.departmentservice.service;

import com.departmentservice.hibernate.HibernateUtils;
import com.departmentservice.model.DTO.DepartmentDTO;
import com.departmentservice.model.Department;
import com.departmentservice.model.mapper.DepartmentMapper;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService {

    private static final Logger logger = LoggerFactory.getLogger(DepartmentService.class);


    public List<DepartmentDTO> findAll() {
        List<DepartmentDTO> departmentDTOs = new ArrayList<>();
        try (Session session = HibernateUtils.startSession()) {
            List<Department> departments = session.createQuery("FROM Department", Department.class).list();
            for (Department department : departments) {
                DepartmentDTO departmentDTO = DepartmentMapper.INSTANCE.departmentToDepartmentDTO(department);
                departmentDTOs.add(departmentDTO);
            }
        } catch (Exception e) {
            logger.error("Error in findAll: ", e);
        }
        return departmentDTOs;
    }

    public DepartmentDTO findById(Long id) {
        DepartmentDTO departmentDTO = null;
        try (Session session = HibernateUtils.startSession()) {
            Department department = session.get(Department.class, id);
            if (department != null) {
                departmentDTO = DepartmentMapper.INSTANCE.departmentToDepartmentDTO(department);
            }
        } catch (Exception e) {
            logger.error("Error in findById: ", e);
        }
        return departmentDTO;
    }

    public DepartmentDTO saveDepartment(DepartmentDTO departmentDTO) {
        Session session = null;
        Transaction transaction = null;
        try {
            Department department = DepartmentMapper.INSTANCE.departmentDTOToDepartment(departmentDTO);
            session = HibernateUtils.startSession();
            transaction = session.beginTransaction();
            session.save(department);
            transaction.commit();
            departmentDTO.setId(department.getId());
        } catch (Exception e) {
            logger.error("Error in saveDepartment: ", e);
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                HibernateUtils.closeSession();
            }
        }
        return departmentDTO;
    }

    public void deleteById(Long id) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtils.startSession();
            Department department = session.get(Department.class, id);
            if (department == null) {
                throw new Exception("Отдел не найден: " + id);
            }
            transaction = session.beginTransaction();
            session.delete(department);
            transaction.commit();
        } catch (Exception e) {
            logger.error("Error in deleteById: ", e);
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                HibernateUtils.closeSession();
            }
        }
    }
}
