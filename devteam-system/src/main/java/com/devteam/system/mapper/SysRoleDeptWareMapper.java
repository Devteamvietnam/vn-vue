package com.devteam.system.mapper;

import java.util.List;

import com.devteam.system.domain.SysRoleDeptWare;

/**
 * Role and department association table Data layer
 *
 */
public interface SysRoleDeptWareMapper {

    /**
     * Delete role and department association by role ID
     *
     * @param roleId role ID
     * @return result
     */
    public int deleteRoleDeptWareByRoleId(Long roleId);

    /**
     * Batch delete role department related information
     *
     * @param ids ID of the data to be deleted
     * @return result
     */
    public int deleteRoleDeptWare(Long[] ids);

    /**
     * Query the number of departments used
     *
     * @param deptId department ID
     * @return result
     */
    public int selectCountRoleDeptWareByDeptId(Long deptId);

    /**
     * Add role department information in batches
     *
     * @param roleDeptList role department list
     * @return result
     */
    public int batchRoleDeptWare(List<SysRoleDeptWare> roleDeptList);
}
