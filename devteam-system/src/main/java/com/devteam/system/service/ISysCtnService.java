package com.devteam.system.service;

import java.util.List;

import com.devteam.common.core.domain.TreeSelect;
import com.devteam.common.core.domain.entity.SysCtn;

/**
 * Department management service layer
 */
public interface ISysCtnService
{
    /**
     * Query ctn management data
     *
     * @param ctn ctn information
     * @return ctn information collection
     */
    public List<SysCtn> selectCtnList(SysCtn ctn);

    /**
     * Build the tree structure required by the front end
     *
     * @param ctns ctn list
     * @return tree structure list
     */
    public List<SysCtn> buildCtnTree(List<SysCtn> ctns);

    /**
     * Build the drop-down tree structure required by the front end
     *
     * @param ctns ctn list
     * @return drop-down tree structure list
     */
    public List<TreeSelect> buildCtnTreeSelect(List<SysCtn> ctns);

    /**
     * Query ctn tree information based on role ID
     *
     * @param roleId role ID
     * @return selected department list
     */
    public List<Integer> selectCtnListByRoleId(Long roleId);

    /**
     * Query information based on department ID
     *
     * @param deptId department ID
     * @return department information
     */
    public SysCtn selectCtnById(Long ctnId);

    /**
     * Query all sub-departments based on ID (normal state)
     *
     * @param ctnId department ID
     * @return Number of sub-departments
     */
    public int selectNormalChildrenCtnById(Long ctnId);

    /**
     * Whether there are department sub-nodes
     *
     * @param ctnId department ID
     * @return result
     */
    public boolean hasChildByCtnId(Long ctnId);

    /**
     * Query whether there are users in the department
     *
     * @param ctnId department ID
     * @return result true exists false does not exist
     */
    public boolean checkCtnExistUser(Long ctnId);

    /**
     * Check whether the department name is unique
     *
     * @param ctn department information
     * @return result
     */
    public String checkCtnNameUnique(SysCtn ctn);

    /**
     * Added save department information
     *
     * @param ctn department information
     * @return result
     */
    public int insertCtn(SysCtn ctn);

    /**
     * Modify and save department information
     *
     * @param ctn department information
     * @return result
     */
    public int updateCtn(SysCtn ctn);

    /**
     * Delete ctn management information
     *
     * @param ctnId department ID
     * @return result
     */
    public int deleteCtnById(Long ctnId);
}
