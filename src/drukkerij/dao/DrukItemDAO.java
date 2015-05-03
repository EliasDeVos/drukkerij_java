package drukkerij.dao;


import drukkerij.model.DrukItem;

import java.util.List;


public interface DrukItemDAO {
    public void addDrukOrder(DrukItem drukItem);

    public List<DrukItem> listDrukOrder();

    public void removeDrukOrder(Integer id);

    public void updateDrukOrder(DrukItem drukItem) throws Exception;

    public DrukItem getDrukItem(Integer drukitemId);
}