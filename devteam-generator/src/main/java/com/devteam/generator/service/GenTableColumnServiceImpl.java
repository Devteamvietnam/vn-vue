package com.devteam.generator.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.devteam.common.core.text.Convert;
import com.devteam.generator.domain.GenTableColumn;
import com.devteam.generator.mapper.GenTableColumnMapper;

/**
 * Business field service layer implementation
 */
@Service
public class GenTableColumnServiceImpl implements IGenTableColumnService {
	@Autowired
	private GenTableColumnMapper genTableColumnMapper;

	/**
	 * Query the list of business fields
	 *
	 * @param tableId business field number
	 * @return business field collection
	 */
	@Override
	public List<GenTableColumn> selectGenTableColumnListByTableId(Long tableId) {
		return genTableColumnMapper.selectGenTableColumnListByTableId(tableId);
	}

	/**
	 * Added business fields
	 *
	 * @param genTableColumn business field information
	 * @return result
	 */
	@Override
	public int insertGenTableColumn(GenTableColumn genTableColumn) {
		return genTableColumnMapper.insertGenTableColumn(genTableColumn);
	}

	/**
	 * Modify business fields
	 *
	 * @param genTableColumn business field information
	 * @return result
	 */
	@Override
	public int updateGenTableColumn(GenTableColumn genTableColumn) {
		return genTableColumnMapper.updateGenTableColumn(genTableColumn);
	}

	/**
	 * Delete business field object
	 *
	 * @param ids ID of the data to be deleted
	 * @return result
	 */
	@Override
	public int deleteGenTableColumnByIds(String ids) {
		return genTableColumnMapper.deleteGenTableColumnByIds(Convert.toLongArray(ids));
	}
}
