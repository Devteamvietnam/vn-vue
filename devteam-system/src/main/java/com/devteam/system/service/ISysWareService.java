package com.devteam.system.service;

import java.util.List;

import com.devteam.system.domain.SysWare;

/**
 * Ware information service layer
 */
public interface ISysWareService
{
    /**
     * Query job information collection
     *
     * @param ware job information
     * @return ware list
     */
    public List<SysWare> selectWareList(SysWare ware);

    /**
     * Query all positions
     *
     * @return ware list
     */
    public List<SysWare> selectWareAll();

    /**
     * Query job information through job ID
     *
     * @param wareId ware ID
     * @return role object information
     */
    public SysWare selectWareById(Long wareId);

    /**
     * Get the list of post selection boxes according to the user ID
     *
     * @param userId user ID
     * @return Selected ware ID list
     */
    public List<Integer> selectWareListByUserId(Long userId);

    /**
     * Check post name
     *
     * @param post job information
     * @return result
     */
    public String checkWareNameUnique(SysWare ware);

    /**
     * Check ware code
     *
     * @param ware warehouse information
     * @return result
     */
    public String checkWareCodeUnique(SysWare ware);

    /**
     * Query the number of positions used by the position ID
     *
     * @param wareId ware ID
     * @return result
     */
    public int countUserWareById(Long wareId);

    /**
     * Delete post information
     *
     * @param wareId ware ID
     * @return result
     */
    public int deleteWareById(Long wareId);

    /**
     * Delete job information in bulk
     *
     * @param wareIds ID of the ware to be deleted
     * @return result
     * @throws Exception
     */
    public int deleteWareByIds(Long[] wareIds);

    /**
     * Added save ware information
     *
     * @param ware job information
     * @return result
     */
    public int insertWare(SysWare ware);

    /**
     * Modify and save ware information
     *
     * @param ware ware information
     * @return result
     */
    public int updateWare(SysWare ware);
}
