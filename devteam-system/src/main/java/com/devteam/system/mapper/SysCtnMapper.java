package com.devteam.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.devteam.common.core.domain.entity.SysCtn;


/**
 * Ctn management data layer
 */
public interface SysCtnMapper
{
    /**
     * Query ctn management data
     *
     * @param ctn ctn information
     * @return ctn information collection
     */
    public List<SysCtn> selectCtnList(SysCtn ctn);

    /**
     * Query ctn tree information based on role ID
     *
     * @param roleId role ID
     * @param ctnCheckStrictly whether the ctn tree selection item is displayed in association
     * @return selected ctn list
     */
    public List<Integer> selectCtnListByRoleId(@Param("roleId") Long roleId, @Param("ctnCheckStrictly") boolean ctnCheckStrictly);

    /**
     * Query information based on department ID
     *
     * @param ctnId ctn ID
     * @return ctn information
     */
    public SysCtn selectCtnById(Long ctnId);

    /**
     * Query all sub-departments based on ID
     *
     * @param ctnId ctn ID
     * @return CTN list
     */
    public List<SysCtn> selectChildrenCtnById(Long ctnId);

    /**
     * Query all sub-departments based on ID (normal state)
     *
     * @param ctnId ctn ID
     * @return Number of sub-departments
     */
    public int selectNormalChildrenCtnById(Long ctnId);

    /**
     * Whether there are child nodes
     *
     * @param ctnId ctn ID
     * @return result
     */
    public int hasChildByCtnId(Long ctnId);

    /**
     * Query whether there are users in the department
     *
     * @param ctnId ctn ID
     * @return result
     */
    public int checkCtnExistUser(Long ctnId);

    /**
     * Check whether the department name is unique
     *
     * @param ctnName ctn name
     * @param parentId parent ctn ID
     * @return result
     */
    public SysCtn checkCtnNameUnique(@Param("ctnName") String ctnName, @Param("parentId") Long parentId);

    /**
     * Add ctn information
     *
     * @param ctn department information
     * @return result
     */
    public int insertCtn(SysCtn ctn);

    /**
     * Modify department information
     *
     * @param ctn department information
     * @return result
     */
    public int updateCtn(SysCtn ctn);

    /**
     * Modify the status of the parent ctn of the ctn
     *
     * @param ctn department
     */
    public void updateCtnStatus(SysCtn ctn);

    /**
     * Modify the child element relationship
     *
     * @param ctns child element
     * @return result
     */
    public int updateCtnChildren(@Param("ctns") List<SysCtn> ctns);

    /**
     * Delete ctn management information
     *
     * @param ctnId department ID
     * @return result
     */
    public int deleteCtnById(Long ctnId);
}
