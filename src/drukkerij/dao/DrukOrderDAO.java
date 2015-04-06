package drukkerij.dao;


import drukkerij.model.DrukOrder;

import java.util.List;


public interface DrukOrderDAO {
    public void addDrukOrder(DrukOrder drukOrder);

    public List<DrukOrder> listDrukOrder();

    public void removeDrukOrder(Integer id);

    public void updateDrukOrder(DrukOrder drukOrder) throws Exception;
}