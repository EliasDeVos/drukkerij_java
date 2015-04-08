package drukkerij.dao;

import drukkerij.model.DrukItem;
import drukkerij.model.HibernateUtil;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;



public class DrukItemDAOImpl implements DrukItemDAO {
    @Override
    public void addDrukOrder(DrukItem drukItem) {
        Session s = HibernateUtil.openSession();
        s.beginTransaction();
        s.save(drukItem);
        s.getTransaction().commit();
        s.close();
    }

    @Override
    public List<DrukItem> listDrukOrder() {
        List<DrukItem> list = new ArrayList<>();
        Session s = HibernateUtil.openSession();
        s.beginTransaction();
        list = s.createQuery("from DrukItem").list();
        s.getTransaction().commit();
        s.close();
        return list;
    }

    @Override
    public void removeDrukOrder(Integer id) {
        Session s = HibernateUtil.openSession();
        s.beginTransaction();
        DrukItem c = (DrukItem) s.load(DrukItem.class, id);
        s.delete(c);
        s.getTransaction().commit();
        s.close();
    }

    @Override
    public void updateDrukOrder(DrukItem drukItem) throws Exception{
        Session s = HibernateUtil.openSession();
        s.beginTransaction();
        s.update(drukItem);
        s.getTransaction().commit();
        s.close();
    }
}