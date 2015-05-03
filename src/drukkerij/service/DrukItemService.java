package drukkerij.service;


import drukkerij.model.DrukItem;

import java.util.List;

/**
 * Created by Pol on 23/02/2015.
 */
public interface DrukItemService {
    public void addDrukOrder(DrukItem drukItem);

    public List<DrukItem> listDrukOrder();

    public void removeDrukOrder(Integer id);

    public void updateDrukOrder(DrukItem drukItem) throws Exception;

    public DrukItem getDrukItem(Integer drukItemId);

}
