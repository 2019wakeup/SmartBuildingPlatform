# Service层和Mapper层适配说明

## 适配概述

根据重构后的数据实体结构，我已经完成了Service层和Mapper层的全面适配工作，包括新增服务、更新现有服务以及创建对应的Mapper XML文件。

## 1. 新增的Service接口和实现

### 1.1 IBreathSampleService & BreathSampleServiceImpl
- **功能**: 呼吸样本管理服务
- **主要方法**:
  - `selectBreathSampleList()` - 分页查询呼吸样本列表
  - `selectBreathSampleById()` - 根据ID查询呼吸样本
  - `selectBreathSamplesByUserId()` - 根据用户ID查询呼吸样本
  - `insertBreathSample()` - 新增呼吸样本
  - `updateBreathSample()` - 更新呼吸样本
  - `deleteBreathSampleById()` - 删除呼吸样本
  - `deleteBreathSampleByUserId()` - 根据用户ID删除样本

### 1.2 IUserRolesService & UserRolesServiceImpl
- **功能**: 用户角色关系管理服务
- **主要方法**:
  - `selectUserRolesByUserId()` - 根据用户ID查询角色关系
  - `selectUserRolesByRoleId()` - 根据角色ID查询用户关系
  - `insertUserRoles()` - 新增用户角色关系
  - `batchInsertUserRoles()` - 批量新增用户角色关系
  - `deleteUserRolesByUserId()` - 删除用户的所有角色关系
  - `deleteUserRolesByRoleId()` - 删除角色的所有用户关系

### 1.3 IRolePermissionsService & RolePermissionsServiceImpl
- **功能**: 角色权限关系管理服务
- **主要方法**:
  - `selectRolePermissionsByRoleId()` - 根据角色ID查询权限关系
  - `selectRolePermissionsByPermissionId()` - 根据权限ID查询角色关系
  - `insertRolePermissions()` - 新增角色权限关系
  - `batchInsertRolePermissions()` - 批量新增角色权限关系
  - `deleteRolePermissionsByRoleId()` - 删除角色的所有权限关系
  - `deleteRolePermissionsByPermissionId()` - 删除权限的所有角色关系

### 1.4 IRoleService & RoleServiceImpl
- **功能**: 角色管理服务（重新实现）
- **主要方法**:
  - `selectRoleList()` - 分页查询角色列表
  - `selectRolesByUserId()` - 根据用户ID查询角色
  - `selectRoleAll()` - 查询所有角色
  - `insertRole()` - 新增角色
  - `updateRole()` - 更新角色
  - `deleteRoleById()` - 删除角色（级联删除角色权限关系）
  - `insertRoleAuth()` - 角色授权权限

### 1.5 IPermissionService & PermissionServiceImpl
- **功能**: 权限管理服务
- **主要方法**:
  - `selectPermissionList()` - 分页查询权限列表
  - `selectPermissionsByRoleId()` - 根据角色ID查询权限
  - `selectPermissionAll()` - 查询所有权限
  - `selectPermissionByCode()` - 根据权限代码查询权限
  - `checkPermissionCodeUnique()` - 校验权限代码唯一性
  - `insertPermission()` - 新增权限
  - `updatePermission()` - 更新权限
  - `deletePermissionById()` - 删除权限（级联删除角色权限关系）

## 2. 新增的Mapper接口

### 2.1 BreathSampleMapper
- **映射表**: BreathSample
- **继承**: BaseMapper<BreathSample>
- **XML文件**: BreathSampleMapper.xml

### 2.2 UserRolesMapper
- **映射表**: UserRoles
- **继承**: BaseMapper<UserRoles>
- **XML文件**: UserRolesMapper.xml

### 2.3 RolePermissionsMapper
- **映射表**: RolePermissions
- **继承**: BaseMapper<RolePermissions>
- **XML文件**: RolePermissionsMapper.xml

### 2.4 PermissionMapper
- **映射表**: Permission
- **继承**: BaseMapper<Permission>
- **XML文件**: PermissionMapper.xml

## 3. 更新的现有服务

### 3.1 UserServiceImpl
- **更新内容**:
  - 启用了 `@Service` 注解
  - 添加了 `IUserRolesService` 依赖注入
  - 重新实现了 `insertUserAuth()` 方法，支持用户角色关系管理
  - 保持了其他方法的兼容性

### 3.2 RoleMapper
- **更新内容**:
  - 移除了 `checkRoleKeyUnique()` 方法（不再需要roleKey字段）
  - 保持了其他方法签名的兼容性

## 4. 新增的Mapper XML文件

### 4.1 BreathSampleMapper.xml
- **ResultMap**: BreathSampleResult
- **关联映射**: User对象
- **支持操作**: CRUD + 按用户查询

### 4.2 UserRolesMapper.xml
- **ResultMap**: UserRolesResult
- **关联映射**: User和Role对象
- **支持操作**: 关系管理 + 批量操作

### 4.3 RolePermissionsMapper.xml
- **ResultMap**: RolePermissionsResult
- **关联映射**: Role和Permission对象
- **支持操作**: 关系管理 + 批量操作

### 4.4 PermissionMapper.xml
- **ResultMap**: PermissionResult
- **支持操作**: CRUD + 按角色查询权限

### 4.5 RoleMapper.xml（新建）
- **ResultMap**: RoleResult
- **关联映射**: User和Permission对象
- **支持操作**: CRUD + 复杂查询

## 5. 更新的现有XML文件

### 5.1 UserMapper.xml
- **移除字段**: status, delFlag
- **新增关联**: Role和BreathSample对象映射
- **更新查询**: 移除软删除相关条件
- **更新操作**: 改为物理删除

## 6. 关键特性

### 6.1 事务管理
- 所有写操作都添加了 `@Transactional` 注解
- 级联删除操作确保数据一致性

### 6.2 关联查询
- 支持对象关联映射（User-Role, User-BreathSample等）
- 提供了灵活的查询方法

### 6.3 批量操作
- 支持批量插入用户角色关系
- 支持批量插入角色权限关系
- 支持批量删除操作

### 6.4 数据验证
- 保持了唯一性校验方法
- 支持代码和名称的重复检查

## 7. 使用示例

### 7.1 用户角色管理
```java
// 为用户分配角色
Long userId = 1L;
Long[] roleIds = {1L, 2L};
userService.insertUserAuth(userId, roleIds);

// 查询用户的角色
List<UserRoles> userRoles = userRolesService.selectUserRolesByUserId(userId);
```

### 7.2 角色权限管理
```java
// 为角色分配权限
Long roleId = 1L;
Long[] permissionIds = {1L, 2L, 3L};
roleService.insertRoleAuth(roleId, permissionIds);

// 查询角色的权限
List<Permission> permissions = permissionService.selectPermissionsByRoleId(roleId);
```

### 7.3 呼吸样本管理
```java
// 创建呼吸样本
BreathSample sample = new BreathSample();
sample.setUserId(1L);
sample.setDataTaken(LocalDateTime.now());
breathSampleService.insertBreathSample(sample);

// 查询用户的呼吸样本
List<BreathSample> samples = breathSampleService.selectBreathSamplesByUserId(1L);
```

## 8. 项目状态

- ✅ 新增Service接口和实现类
- ✅ 新增Mapper接口
- ✅ 新增Mapper XML文件
- ✅ 更新现有Service实现
- ✅ 更新现有Mapper XML文件
- ✅ 支持多对多关系管理
- ✅ 支持级联操作
- ✅ 保持向后兼容性

## 9. 后续建议

1. **测试验证**: 编写单元测试验证所有Service和Mapper方法
2. **性能优化**: 对复杂查询添加适当的索引
3. **缓存策略**: 考虑为频繁查询的数据添加缓存
4. **监控日志**: 添加操作日志记录关键业务操作
5. **文档更新**: 更新API文档以反映新的服务接口

Service层和Mapper层的适配工作已经完成，新的架构支持更灵活的用户角色权限管理，同时保持了良好的扩展性和维护性。 