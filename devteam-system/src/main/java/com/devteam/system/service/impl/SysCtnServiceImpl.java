package com.devteam.system.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.devteam.common.annotation.DataScope;
import com.devteam.common.constant.UserConstants;
import com.devteam.common.core.domain.TreeSelect;
import com.devteam.common.core.domain.entity.SysCtn;
import com.devteam.common.core.domain.entity.SysDept;
import com.devteam.common.core.domain.entity.SysRole;
import com.devteam.common.exception.CustomException;
import com.devteam.common.utils.StringUtils;
import com.devteam.system.mapper.SysCtnMapper;
import com.devteam.system.mapper.SysRoleMapper;
import com.devteam.system.service.ISysCtnService;

public class SysCtnServiceImpl implements ISysCtnService {

	  	@Autowired
	    private SysCtnMapper ctnMapper;

	    @Autowired
	    private SysRoleMapper roleMapper;

	    /**
	     * Query department management data
	     *
	     * @param ctn department information
	     * @return ctn information collection
	     */
	    @Override
	    @DataScope(deptAlias = "d")
	    public List<SysCtn> selectCtnList(SysCtn ctn)
	    {
	        return ctnMapper.selectCtnList(ctn);
	    }

	    /**
	     * Build the tree structure required by the front end
	     *
	     * @param ctns department list
	     * @return tree structure list
	     */
	    @Override
	    public List<SysCtn> buildCtnTree(List<SysCtn> ctns)
	    {
	        List<SysCtn> returnList = new ArrayList<SysCtn>();
	        List<Long> tempList = new ArrayList<Long>();
	        for (SysCtn ctn: ctns)
	        {
	            tempList.add(ctn.getCtnId());
	        }
	        for (Iterator<SysCtn> iterator = ctns.iterator(); iterator.hasNext();)
	        {
	        	SysCtn ctn = (SysCtn) iterator.next();
	            // If it is a top-level node, traverse all child nodes of the parent node
	            if (!tempList.contains(ctn.getParentId()))
	            {
	                recursionFn(ctns, ctn);
	                returnList.add(ctn);
	            }
	        }
	        if (returnList.isEmpty())
	        {
	            returnList = ctns;
	        }
	        return returnList;
	    }

	    /**
	     * Build the drop-down tree structure required by the front end
	     *
	     * @param ctns ctn list
	     * @return drop-down tree structure list
	     */
	    @Override
	    public List<TreeSelect> buildCtnTreeSelect(List<SysCtn> ctns)
	    {
	        List<SysCtn> deptTrees = buildCtnTree(ctns);
	        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
	    }

	    /**
	     * Query department tree information based on role ID
	     *
	     * @param roleId role ID
	     * @return selected department list
	     */
	    @Override
	    public List<Integer> selectCtnListByRoleId(Long roleId)
	    {
	        SysRole role = roleMapper.selectRoleById(roleId);
	        return ctnMapper.selectCtnListByRoleId(roleId, role.isCtnCheckStrictly());
	    }

	    /**
	     * Query information based on department ID
	     *
	     * @param ctnId department ID
	     * @return ctn information
	     */
	    @Override
	    public SysCtn selectCtnById(Long ctnId)
	    {
	        return ctnMapper.selectCtnById(ctnId);
	    }

	    /**
	     * Query all sub-departments based on ID (normal state)
	     *
	     * @param ctnId department ID
	     * @return Number of sub-departments
	     */
	    @Override
	    public int selectNormalChildrenCtnById(Long ctnId)
	    {
	        return ctnMapper.selectNormalChildrenCtnById(ctnId);
	    }

	    /**
	     * Whether there are child nodes
	     *
	     * @param ctnId department ID
	     * @return result
	     */
	    @Override
	    public boolean hasChildByCtnId(Long ctnId)
	    {
	        int result = ctnMapper.hasChildByCtnId(ctnId);
	        return result> 0? true: false;
	    }

	    /**
	     * Query whether there are users in the department
	     *
	     * @param ctnId department ID
	     * @return result true exists false does not exist
	     */
	    @Override
	    public boolean checkCtnExistUser(Long ctnId)
	    {
	        int result = ctnMapper.checkCtnExistUser(ctnId);
	        return result> 0? true: false;
	    }

	    /**
	     * Check whether the department name is unique
	     *
	     * @param ctn department information
	     * @return result
	     */
	    @Override
	    public String checkCtnNameUnique(SysCtn ctn)
	    {
	        Long ctnId = StringUtils.isNull(ctn.getCtnId())? -1L: ctn.getCtnId();
	        SysCtn info = ctnMapper.checkCtnNameUnique(ctn.getCtnName(), ctn.getParentId());
	        if (StringUtils.isNotNull(info) && info.getCtnId().longValue() != ctnId.longValue())
	        {
	            return UserConstants.NOT_UNIQUE;
	        }
	        return UserConstants.UNIQUE;
	    }

	    /**
	     * Added save department information
	     *
	     * @param ctn department information
	     * @return result
	     */
	    @Override
	    public int insertCtn(SysCtn ctn)
	    {
	    	SysCtn info = ctnMapper.selectCtnById(ctn.getParentId());
	        // If the parent node is not in the normal state, no new child nodes are allowed
	        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus()))
	        {
	            throw new CustomException("CTN is disabled, adding is not allowed");
	        }
	        ctn.setAncestors(info.getAncestors() + "," + ctn.getParentId());
	        return ctnMapper.insertCtn(ctn);
	    }

	    /**
	     * Modify and save department information
	     *
	     * @param ctn department information
	     * @return result
	     */
	    @Override
	    public int updateCtn(SysCtn ctn)
	    {
	    	SysCtn newParentCtn = ctnMapper.selectCtnById(ctn.getParentId());
	    	SysCtn oldDept = ctnMapper.selectCtnById(ctn.getCtnId());
	        if (StringUtils.isNotNull(newParentCtn) && StringUtils.isNotNull(oldDept))
	        {
	            String newAncestors = newParentCtn.getAncestors() + "," + newParentCtn.getCtnId();
	            String oldAncestors = oldDept.getAncestors();
	            ctn.setAncestors(newAncestors);
	            updateCtnChildren(ctn.getCtnId(), newAncestors, oldAncestors);
	        }
	        int result = ctnMapper.updateCtn(ctn);
	        if (UserConstants.DEPT_NORMAL.equals(ctn.getStatus()))
	        {
	            // If the department is enabled, all superior departments of the department are enabled
	        	updateParentCtnStatus(ctn);
	        }
	        return result;
	    }

	    /**
	     * Modify the status of the parent department of the department
	     *
	     * @param ctn current department
	     */
	    private void updateParentCtnStatus(SysCtn ctn)
	    {
	        String updateBy = ctn.getUpdateBy();
	        ctn = ctnMapper.selectCtnById(ctn.getCtnId());
	        ctn.setUpdateBy(updateBy);
	        ctnMapper.updateCtnStatus(ctn);
	    }

	    /**
	     * Modify the child element relationship
	     *
	     * @param ctnId Department ID being modified
	     * @param newAncestors new parent ID collection
	     * @param oldAncestors old parent ID collection
	     */
	    public void updateCtnChildren(Long ctnId, String newAncestors, String oldAncestors)
	    {
	        List<SysCtn> children = ctnMapper.selectChildrenCtnById(ctnId);
	        for (SysCtn child: children)
	        {
	            child.setAncestors(child.getAncestors().replace(oldAncestors, newAncestors));
	        }
	        if (children.size()> 0)
	        {
	            ctnMapper.updateCtnChildren(children);
	        }
	    }

	    /**
	     * Delete department management information
	     *
	     * @param ctnId department ID
	     * @return result
	     */
	    @Override
	    public int deleteCtnById(Long ctnId)
	    {
	        return ctnMapper.deleteCtnById(ctnId);
	    }

	    /**
	     * Recursive list
	     */
	    private void recursionFn(List<SysCtn> list, SysCtn t)
	    {
	        // Get the list of child nodes
	        List<SysCtn> childList = getChildList(list, t);
	        t.setChildren(childList);
	        for (SysCtn tChild: childList)
	        {
	            if (hasChild(list, tChild))
	            {
	                recursionFn(list, tChild);
	            }
	        }
	    }

	    /**
	     * Get the list of child nodes
	     */
	    private List<SysCtn> getChildList(List<SysCtn> list, SysCtn t)
	    {
	        List<SysCtn> tlist = new ArrayList<SysCtn>();
	        Iterator<SysCtn> it = list.iterator();
	        while (it.hasNext())
	        {
	        	SysCtn n = (SysCtn) it.next();
	            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getCtnId().longValue())
	            {
	                tlist.add(n);
	            }
	        }
	        return tlist;
	    }

	    /**
	     * Determine whether there are child nodes
	     */
	    private boolean hasChild(List<SysCtn> list, SysCtn t)
	    {
	        return getChildList(list, t).size()> 0? true: false;
	    }

}
