package com.devteam.system.mapper;

import java.util.List;

import com.devteam.system.domain.SysWare;

/**
 *  WareHouse information data layer
 * @author jungj
 *
 */
public interface SysWareHouseMapper {

	   /**
     * Query warehouse data collection
     *
     * @param ware ware information
     * @return ware data collection
     */
	
    public List<SysWare> selectWareList(SysWare ware);
    
    /**
     * Query all warehouse
     *
     * @return ware list
     */
    
    public List<SysWare> selectWareAll();
    
    /**
     * Query ware information through ware ID
     *
     * @param wareId ware ID
     * @return role object information
     */
    
    public SysWare selectWareById(Long wareId);
    
    /**
     * Get the list of ware selection boxes according to the user ID
     *
     * @param userId user ID
     * @return Selected ware ID list
     */
    public List<Integer> selectWareListByUserId(Long userId);

    /**
     * Query the ware group that the user belongs to
     *
     * @param userName username
     * @return result
     */
    public List<SysWare> selectPostsByUserName(String userName);
    
    /**
     * Delete ware information
     *
     * @param wareId ware ID
     * @return result
     */
    public int deleteWareById(Long wareId);
    
    /**
     * Delete ware information in bulk
     *
     * @param wareIds ID of the ware to be deleted
     * @return result
     */
    public int deleteWareByIds(Long[] wareIds);
    
    /**
     * Modify ware information
     *
     * @param ware ware information
     * @return result
     */
    public int updateWare(SysWare ware);
    
    /**
     * New ware information
     *
     * @param ware warehouse information
     * @return result
     */
    public int insertWare(SysWare ware);
    
    /**
     * Check ware name
     *
     * @param wareName ware name
     * @return result
     */
    public SysWare checkWareNameUnique(String wareName);

    /**
     * Check ware code
     *
     * @param wareCode post code
     * @return result
     */
    public SysWare checkWareCodeUnique(String wareCode);
}
