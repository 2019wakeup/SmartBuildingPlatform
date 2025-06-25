package org.example.smartcloudplatform.controller.system;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.smartcloudplatform.common.AjaxResult;
import org.example.smartcloudplatform.common.TableDataInfo;
import org.example.smartcloudplatform.entity.User;
import org.example.smartcloudplatform.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.example.smartcloudplatform.common.JwtUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * 用户信息
 * 
 * @author SmartCloudPlatform
 */
@Tag(name = "用户管理", description = "用户信息的增删改查操作")
@RestController
@RequestMapping("/system/user")
public class SysUserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Value("${upload.path}")
    private String uploadPath;

    /**
     * 获取用户列表
     */
    @Operation(summary = "获取用户列表", description = "分页查询用户信息列表")
    @GetMapping("/list")
    public TableDataInfo list(
            @Parameter(description = "用户查询条件") User user) {
        List<User> list = userService.selectUserList(user);
        return TableDataInfo.getDataTable(list);
    }

    /**
     * 根据用户编号获取详细信息
     */
    @Operation(summary = "获取用户详情", description = "根据用户ID获取用户详细信息")
    @GetMapping(value = {"/", "/{userId}"})
    public AjaxResult getInfo(
            @Parameter(description = "用户ID", example = "1") 
            @PathVariable(value = "userId", required = false) Long userId) {
        AjaxResult ajax = AjaxResult.success();
        if (userId != null) {
            User sysUser = userService.selectUserById(userId);
            ajax.put(AjaxResult.DATA_TAG, sysUser);
        }
        return ajax;
    }

    /**
     * 个人信息
     */
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的个人信息")
    @GetMapping("/profile")
    public AjaxResult profile(@RequestHeader("Authorization") String token) {
        token = token.startsWith("Bearer ") ? token.substring(7) : token;
        Long userId = jwtUtils.getUserIdFromToken(token);
        User user = userService.selectUserById(userId);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        return ajax;
    }

    /**
     * 修改个人信息
     */
    @Operation(summary = "修改当前用户信息", description = "修改当前登录用户的个人信息")
    @PutMapping("/profile")
    public AjaxResult updateProfile(@RequestHeader("Authorization") String token, @RequestBody User user) {
        token = token.startsWith("Bearer ") ? token.substring(7) : token;
        Long loginUserId = jwtUtils.getUserIdFromToken(token);
        User loginUser = userService.selectUserById(loginUserId);

        loginUser.setUserName(user.getUserName());
        loginUser.setPhone(user.getPhone());
        loginUser.setEmail(user.getEmail());

        if (userService.updateUserProfile(loginUser) > 0) {
            return AjaxResult.success();
        }
        return AjaxResult.error("修改个人信息异常，请联系管理员");
    }

    /**
     * 头像上传
     */
    @Operation(summary = "用户头像上传", description = "上传并更新当前用户的头像")
    @PostMapping("/avatar")
    public AjaxResult avatar(@RequestHeader("Authorization") String token, @RequestParam("avatarfile") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            token = token.startsWith("Bearer ") ? token.substring(7) : token;
            Long userId = jwtUtils.getUserIdFromToken(token);

            // 获取文件名
            String fileName = file.getOriginalFilename();
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            // 生成新的文件名
            fileName = UUID.randomUUID().toString() + suffixName;

            String filePath = uploadPath + "/avatars/";
            File dest = new File(filePath + fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
            String avatarUrl = "/uploads/avatars/" + fileName;

            if (userService.updateUserAvatar(userId, avatarUrl)) {
                AjaxResult ajax = AjaxResult.success();
                ajax.put("imgUrl", avatarUrl);
                return ajax;
            }
        }
        return AjaxResult.error("上传图片异常，请联系管理员");
    }

    /**
     * 新增用户
     */
    @Operation(summary = "新增用户", description = "创建新的用户信息")
    @PostMapping
    public AjaxResult add(
            @Parameter(description = "用户信息") 
            @Validated @RequestBody User user) {
        if (!userService.checkUserNameUnique(user)) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (!userService.checkPhoneUnique(user)) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (!userService.checkEmailUnique(user)) {
            return AjaxResult.error("新增用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        
        return AjaxResult.success(userService.insertUser(user));
    }

    /**
     * 修改用户
     */
    @Operation(summary = "修改用户", description = "更新用户信息")
    @PutMapping
    public AjaxResult edit(
            @Parameter(description = "用户信息") 
            @Validated @RequestBody User user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        if (!userService.checkUserNameUnique(user)) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (!userService.checkPhoneUnique(user)) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        } else if (!userService.checkEmailUnique(user)) {
            return AjaxResult.error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        
        return AjaxResult.success(userService.updateUser(user));
    }

    /**
     * 删除用户
     */
    @Operation(summary = "删除用户", description = "批量删除用户信息")
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(
            @Parameter(description = "用户ID数组", example = "1,2,3") 
            @PathVariable Long[] userIds) {
        if (userIds.length == 1 && User.isAdmin(userIds[0])) {
            return AjaxResult.error("不能删除超级管理员用户");
        }
        return AjaxResult.success(userService.deleteUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    @Operation(summary = "重置密码", description = "重置用户密码为默认密码123456")
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(
            @Parameter(description = "用户信息") 
            @RequestBody User user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setPassword("123456"); // 默认密码
        return AjaxResult.success(userService.resetPwd(user));
    }

    /**
     * 状态修改
     */
    @Operation(summary = "修改用户状态", description = "启用或停用用户")
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(
            @Parameter(description = "用户信息") 
            @RequestBody User user) {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        return AjaxResult.success(userService.updateUserStatus(user));
    }

    /**
     * 根据用户编号获取授权角色
     */
    @Operation(summary = "获取用户角色", description = "根据用户ID获取用户的角色信息")
    @GetMapping("/authRole/{userId}")
    public AjaxResult authRole(
            @Parameter(description = "用户ID", example = "1") 
            @PathVariable("userId") Long userId) {
        AjaxResult ajax = AjaxResult.success();
        User user = userService.selectUserById(userId);
        ajax.put("user", user);
        return ajax;
    }

    /**
     * 用户授权角色
     */
    @Operation(summary = "用户授权角色", description = "为用户分配角色")
    @PutMapping("/authRole")
    public AjaxResult insertAuthRole(
            @Parameter(description = "用户ID", example = "1") Long userId, 
            @Parameter(description = "角色ID数组", example = "[1,2]") Long[] roleIds) {
        userService.insertUserAuth(userId, roleIds);
        return AjaxResult.success();
    }

    /**
     * 获取部门树列表
     */
    @Operation(summary = "获取部门树", description = "获取部门树结构数据")
    @GetMapping("/deptTree")
    public AjaxResult deptTree() {
        // 简化实现，返回空的部门树
        return AjaxResult.success();
    }
}