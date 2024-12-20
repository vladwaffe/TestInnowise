package com.imageservice.service;


import com.imageservice.hibernate.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.hibernate.query.Query;

import jakarta.transaction.Transactional;
import com.imageservice.model.ImageModel;

@Service
public class ImageService {


    @Transactional
    public ImageModel saveImageFile(Long workerId, MultipartFile file) {
        Session session = null;
        Transaction transaction = null;
        ImageModel imageModel = null;
        try {
            session = HibernateUtils.startSession();
            transaction = session.beginTransaction();
            imageModel = session.createQuery("FROM ImageModel WHERE worker_id = :workerId", ImageModel.class)
                    .setParameter("workerId", workerId)
                    .uniqueResult();
            if (imageModel != null) {
                throw new Exception("Image already exists for this recipe ID.");
            }

            Byte[] byteObjects = new Byte[file.getBytes().length];

            int i = 0;

            for (byte b : file.getBytes()) {
                byteObjects[i++] = b;
            }

            imageModel = new ImageModel();
            imageModel.setImage(byteObjects);
            imageModel.setWorker_id(workerId);
            session.persist(imageModel);
            transaction.commit();


        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                HibernateUtils.closeSession();
            }
        }
        return imageModel;
    }

    @Transactional
    public byte[] getImageFile(Long workerId) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtils.startSession();
            transaction = session.beginTransaction();
            Query<ImageModel> query = session.createQuery("from ImageModel where worker_id = :workerId", ImageModel.class);
            query.setParameter("workerId", workerId);
            ImageModel imageModel = query.uniqueResult();

            if (imageModel != null) {
                byte[] byteContent = new byte[imageModel.getImage().length];
                for (int i = 0; i < imageModel.getImage().length; i++) {
                    byteContent[i] = imageModel.getImage()[i];
                }
                return byteContent;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                HibernateUtils.closeSession();
            }
        }
        return new byte[0];
    }


    @Transactional
    public boolean deleteImageByWorkerId(Long workerId) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtils.startSession();
            transaction = session.beginTransaction();
            Query<ImageModel> query = session.createQuery("from ImageModel where worker_id = :workerId", ImageModel.class);
            query.setParameter("workerId", workerId);
            ImageModel imageModel = query.uniqueResult();

            if (imageModel != null) {
                session.delete(imageModel);
                transaction.commit();
                return true;
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            if (session != null) {
                HibernateUtils.closeSession();
            }
        }
        return false;
    }

    @Transactional
    public ImageModel editImage(Long workerId, MultipartFile file){
        deleteImageByWorkerId(workerId);
        return saveImageFile(workerId, file);
    }
}