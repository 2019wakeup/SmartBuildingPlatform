package org.example.smartcloudplatform.controller.system;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.smartcloudplatform.common.AjaxResult;
import org.example.smartcloudplatform.common.TableDataInfo;
import org.example.smartcloudplatform.entity.Role;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色信息
 * 
 * @author SmartCloudPlatform
 */
@Tag(name = "角色管理", description = "角色信息的增删改查操作")
@RestController
@RequestMapping("/system/role")
public class SysRoleController {

    /**
     * 获取角色列表
     */
    @Operation(summary = "获取角色列表", description = "分页查询角色信息列表")
    @GetMapping("/list")
    public TableDataInfo list(
            @Parameter(description = "角色查询条件") Role role) {
        // 简化实现，返回模拟数据
        List<Role> list = new ArrayList<>();
        Role adminRole = new Role();
        adminRole.setRoleId(1L);
        adminRole.setRoleName("管理员");
        adminRole.setUserId(1L);
        adminRole.setPermissionId(1L);
        list.add(adminRole);
        
        Role userRole = new Role();
        userRole.setRoleId(2L);
        userRole.setRoleName("普通用户");
        userRole.setUserId(2L);
        userRole.setPermissionId(2L);
        list.add(userRole);
        
        return TableDataInfo.getDataTable(list);
    }

    /**
     * 根据角色编号获取详细信息
     */
    @Operation(summary = "获取角色详情", description = "根据角色ID获取角色详细信息")
    @GetMapping(value = "/{roleId}")
    public AjaxResult getInfo(
            @Parameter(description = "角色ID", example = "1") 
            @PathVariable Long roleId) {
        Role role = new Role();
        role.setRoleId(roleId);
        role.setRoleName("管理员");
        role.setUserId(1L);
        role.setPermissionId(1L);
        return AjaxResult.success(role);
    }

    /**
     * 新增角色
     */
    @Operation(summary = "新增角色", description = "创建新的角色信息")
    @PostMapping
    public AjaxResult add(
            @Parameter(description = "角色信息") 
            @RequestBody Role role) {
        return AjaxResult.success("新增角色成功");
    }

    /**
     * 修改角色
     */
    @Operation(summary = "修改角色", description = "更新角色信息")
    @PutMapping
    public AjaxResult edit(
            @Parameter(description = "角色信息") 
            @RequestBody Role role) {
        return AjaxResult.success("修改角色成功");
    }

    /**
     * 删除角色
     */
    @Operation(summary = "删除角色", description = "批量删除角色信息")
    @DeleteMapping("/{roleIds}")
    public AjaxResult remove(
            @Parameter(description = "角色ID数组", example = "1,2,3") 
            @PathVariable Long[] roleIds) {
        return AjaxResult.success("删除角色成功");
    }

    /**
     * 获取角色选择框列表
     */
    @Operation(summary = "获取角色选项", description = "获取角色选择框数据")
    @GetMapping("/optionselect")
    public AjaxResult optionselect() {
        List<Role> roles = new ArrayList<>();
        Role adminRole = new Role();
        adminRole.setRoleId(1L);
        adminRole.setRoleName("管理员");
        roles.add(adminRole);
        
        Role userRole = new Role();
        userRole.setRoleId(2L);
        userRole.setRoleName("普通用户");
        roles.add(userRole);
        
        return AjaxResult.success(roles);
    }
} 