package drukkerij.dao;

import drukkerij.model.DrukOrder;
import drukkerij.model.HibernateUtil;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;



public class DrukOrderDAOImpl implements DrukOrderDAO {
    @Override
    public void addDrukOrder(DrukOrder drukOrder) {
        Session s = HibernateUtil.openSession();
        s.beginTransaction();
        s.save(drukOrder);
        s.getTransaction().commit();
        s.close();
    }

    @Override
    public List<DrukOrder> listDrukOrder() {
        List<DrukOrder> list = new ArrayList<>();
        Session s = HibernateUtil.openSession();
        s.beginTransaction();
        list = s.createQuery("from DrukOrder").list();
        s.getTransaction().commit();
        s.close();
        return list;
    }

    @Override
    public void removeDrukOrder(Integer id) {
        Session s = HibernateUtil.openSession();
        s.beginTransaction();
        DrukOrder c = (DrukOrder) s.load(DrukOrder.class, id);
        s.delete(c);
        s.getTransaction().commit();
        s.close();
    }

    @Override
    public void updateDrukOrder(DrukOrder drukOrder) {
        Session s = HibernateUtil.openSession();
        s.beginTransaction();
        s.update(drukOrder);
        s.getTransaction().commit();
        s.close();
    }
}