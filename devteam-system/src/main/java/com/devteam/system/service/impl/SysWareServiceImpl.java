package com.devteam.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devteam.common.constant.UserConstants;
import com.devteam.common.exception.CustomException;
import com.devteam.common.utils.StringUtils;
import com.devteam.system.domain.SysWare;
import com.devteam.system.mapper.SysUserWareMapper;
import com.devteam.system.mapper.SysWareMapper;
import com.devteam.system.service.ISysWareService;

/**
 * WareHouse information service layer processing
 */

@Service
public class SysWareServiceImpl implements ISysWareService {
	
	@Autowired
	private SysWareMapper wareMapper;
	
	@Autowired
	private SysUserWareMapper userWareMapper;
	
    /**
     * Query ware information collection
     *
     * @param ware job information
     * @return ware information collection
     */

	@Override
	public List<SysWare> selectWareList(SysWare ware) {
		return wareMapper.selectWareList(ware);
	}

    /**
     * Query all ware
     *
     * @return ware list
     */
	
	@Override
	public List<SysWare> selectWareAll() {
		return wareMapper.selectWareAll();
	}

    /**
     * Query ware information through ware ID
     *
     * @param wareId post ID
     * @return role object information
     */
	
	@Override
	public SysWare selectWareById(Long wareId) {
		return wareMapper.selectWareById(wareId);
	}
	
    /**
     * Get the list of post selection boxes according to the user ID
     *
     * @param userId user ID
     * @return Selected ware ID list
     */

	@Override
	public List<Integer> selectWareListByUserId(Long userId) {
		return wareMapper.selectWareListByUserId(userId);
	}
	
    /**
     * Verify that the ware name is unique
     *
     * @param ware  information
     * @return result
     */

	@Override
	public String checkWareNameUnique(SysWare ware) 
		  {
		        Long wareId = StringUtils.isNull(ware.getWareId())? -1L: ware.getWareId();
		        SysWare info = wareMapper.checkWareNameUnique(ware.getWareName());
		        if (StringUtils.isNotNull(info) && info.getWareId().longValue() != wareId.longValue())
		        {
		            return UserConstants.NOT_UNIQUE;
		        }
		        return UserConstants.UNIQUE;
		  }
	
    /**
     * Check whether the ware code is unique
     *
     * @param ware job information
     * @return result
     */

	@Override
	public String checkWareCodeUnique(SysWare ware) {
		 Long wareId = StringUtils.isNull(ware.getWareId())? -1L: ware.getWareId();
	        SysWare info = wareMapper.checkWareCodeUnique(ware.getWareCode());
	        if (StringUtils.isNotNull(info) && info.getWareId().longValue() != wareId.longValue())
	        {
	            return UserConstants.NOT_UNIQUE;
	        }
	        return UserConstants.UNIQUE;
	}
	
    /**
     * Query the number of positions used by the warehouse ID
     *
     * @param wareId ware ID
     * @return result
     */

	@Override
	public int countUserWareById(Long wareId) {
		return userWareMapper.countUserWareById(wareId);
	}


    /**
     * Delete ware information
     *
     * @param wareId ware ID
     * @return result
     */
	@Override
	public int deleteWareById(Long wareId) {
		return wareMapper.deleteWareById(wareId);
	}

    /**
     * Delete ware information in bulk
     *
     * @param wareIds ID of the ware to be deleted
     * @return result
     * @throws Exception
     */
	
	@Override
	public int deleteWareByIds(Long[] wareIds) {
		  for (Long wareId: wareIds)
	        {
	            SysWare ware = selectWareById(wareId);
	            if (countUserWareById(wareId)> 0)
	            {
	                throw new CustomException(String.format("%1$s has been allocated and cannot be deleted", ware.getWareName()));
	            }
	        }
	        return wareMapper.deleteWareByIds(wareIds);
	}

	 /**
     * Added save ware information
     *
     * @param ware job information
     * @return result
     */
	@Override
	public int insertWare(SysWare ware) {
		return wareMapper.insertWare(ware);
	}

    /**
     * Modify and save ware information
     *
     * @param ware ware information
     * @return result
     */
	
	@Override
	public int updateWare(SysWare ware) {
		return wareMapper.updateWare(ware);
	}

}
