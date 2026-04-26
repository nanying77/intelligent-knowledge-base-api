package com.bujian.aipersnonknowledge.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bujian.aipersnonknowledge.entity.Category;
import com.bujian.aipersnonknowledge.entity.Document;
import com.bujian.aipersnonknowledge.entity.KnowledgeBases;
import com.bujian.aipersnonknowledge.entity.User;
import com.bujian.aipersnonknowledge.service.CategoryService;
import com.bujian.aipersnonknowledge.service.DocumentService;
import com.bujian.aipersnonknowledge.service.KnowledgeService;
import com.bujian.aipersnonknowledge.service.UserService;
import com.bujian.aipersnonknowledge.utils.JwtUtils;
import com.bujian.aipersnonknowledge.vo.AdminVo;
import com.bujian.aipersnonknowledge.vo.KnowledgeBaseTreeVO;
import com.bujian.aipersnonknowledge.vo.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 管理员控制器
 */
@Tag(name = "管理员管理")
@RequestMapping("/admin")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final UserService userService;
    private final DocumentService documentService;
    private final CategoryService categoryService;
    private final KnowledgeService knowledgeService;
    private final JwtUtils jwtUtils;

    /**
     * 检查当前用户是否为管理员
     */
    private boolean isAdmin(HttpServletRequest request) {
        try {
            Integer currentUserId = jwtUtils.getCurrentUserId(request);
            User currentUser = userService.getById(currentUserId);
            return currentUser != null && currentUser.getRole() != null && currentUser.getRole() == 1;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取用户列表
     */
    @Operation(summary = "获取用户列表")
    @GetMapping("/user/list")
    public Result<List<AdminVo>> getUserList(HttpServletRequest request) {
        if (!isAdmin(request)) {
            return Result.error(403, "权限不足，仅管理员可访问");
        }

        try {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.orderByDesc(User::getCreateTime);
            List<User> users = userService.list(queryWrapper);

            // 转换为 AdminVo
            List<AdminVo> adminVos = users.stream()
                    .map(user -> {
                        AdminVo adminVo = new AdminVo();
                        BeanUtils.copyProperties(user, adminVo);
                        return adminVo;
                    })
                    .collect(Collectors.toList());
            return Result.success(adminVos);
        } catch (Exception e) {
            log.error("获取用户列表失败", e);
            return Result.error("获取用户列表失败");
        }
    }

    /**
     * 禁用/启用用户
     */
    @Operation(summary = "禁用/启用用户")
    @PutMapping("/user/{id}/status")
  public Result<?> updateUserStatus(
           @PathVariable String id,
           @RequestParam Integer status,
           HttpServletRequest request) {
        if (!isAdmin(request)) {
            return Result.error(403, "权限不足，仅管理员可访问");
        }

        try {
            User user = userService.getById(id);
            if (user == null) {
                return Result.error("用户不存在");
            }

            // 不能修改自己的状态
            Integer currentUserId = jwtUtils.getCurrentUserId(request);
            if (currentUserId.toString().equals(id)) {
                return Result.error("不能修改自己的状态");
            }

            user.setStatus(status);
            boolean updated = userService.updateById(user);

            if (updated) {
                String action = status == 1 ? "启用" : "禁用";
                log.info("管理员已将用户 {} {}", user.getUsername(), action);
                return Result.success(action + "成功");
            } else {
                return Result.error("操作失败");
            }
        } catch (Exception e) {
            log.error("更新用户状态失败，用户ID: {}", id, e);
            return Result.error("操作失败");
        }
    }

    /**
     * 将普通用户升级为管理员
     */
    @Operation(summary = "将普通用户升级为管理员")
    @PutMapping("/user/{id}/authorize")
  public Result<?> authorizeUser(
          @PathVariable String id,
          HttpServletRequest request) {
      if (!isAdmin(request)) {
          return Result.error(403, "权限不足，仅管理员可访问");
      }

      try {
          User user = userService.getById(id);
          if (user == null) {
              return Result.error("用户不存在");
          }

          // 检查是否已经是管理员
          if (user.getRole() != null && user.getRole() == 1) {
              return Result.error("该用户已是管理员");
          }

          // 不能授予自己管理员权限（逻辑上不会执行到这里，因为 isAdmin 已经检查过）
          Integer currentUserId = jwtUtils.getCurrentUserId(request);
          if (currentUserId.toString().equals(id)) {
              return Result.error("无法给自己授予管理员权限");
          }

          // 升级为管理员
          user.setRole(1);
          boolean updated = userService.updateById(user);

          if (updated) {
              log.info("管理员已将用户 {} 升级为管理员", user.getUsername());
              return Result.success("升级成功");
          } else {
              return Result.error("操作失败");
          }
      } catch (Exception e) {
          log.error("升级用户为管理员失败，用户 ID: {}", id, e);
          return Result.error("操作失败");
      }
  }

  /**
   * 永久删除用户
   */
  @Operation(summary = "永久删除用户")
  @DeleteMapping("/user/{id}")
  public Result<?> deleteUser(
       @PathVariable String id,
       HttpServletRequest request) {
    if (!isAdmin(request)) {
        return Result.error(403, "权限不足，仅管理员可访问");
    }

    try {
        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }

        // 不能删除自己
        Integer currentUserId = jwtUtils.getCurrentUserId(request);
        if (currentUserId.toString().equals(id)) {
            return Result.error("不能删除自己");
        }

        // 执行删除操作
        boolean deleted = userService.removeById(id);

        if (deleted) {
            log.info("管理员已永久删除用户 {} (ID: {})", user.getUsername(), id);
            return Result.success("删除成功");
        } else {
            return Result.error("操作失败");
        }
    } catch (Exception e) {
        log.error("删除用户失败，用户 ID: {}", id, e);
        return Result.error("操作失败");
    }
  }

  /**
   * 将管理员降级为普通用户
   */
  @Operation(summary = "将管理员降级为普通用户")
  @PutMapping("/user/{id}/revoke")
  public Result<?> revokeAdminRole(
        @PathVariable String id,
        HttpServletRequest request) {
    if (!isAdmin(request)) {
        return Result.error(403, "权限不足，仅管理员可访问");
    }

    try {
        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }

        // 检查是否是普通用户
        if (user.getRole() == null || user.getRole() == 0) {
            return Result.error("该用户已是普通用户");
        }

        // 不能剥夺自己的管理员权限
        Integer currentUserId = jwtUtils.getCurrentUserId(request);
        if (currentUserId.toString().equals(id)) {
            return Result.error("不能剥夺自己的管理员权限");
        }

        // 降级为普通用户
        user.setRole(0);
        boolean updated = userService.updateById(user);

        if (updated) {
            log.info("管理员已将用户 {} 降级为普通用户", user.getUsername());
            return Result.success("降级成功");
        } else {
            return Result.error("操作失败");
        }
    } catch (Exception e) {
        log.error("降级用户为普通用户失败，用户 ID: {}", id, e);
        return Result.error("操作失败");
    }
  }

    /**
     * 获取知识库与文档的树形结构列表
     */
    @Operation(summary = "获取知识库与文档的树形结构列表")
    @GetMapping("/document/list")
   public Result<List<KnowledgeBaseTreeVO>> getKnowledgeBaseWithDocuments(HttpServletRequest request) {
        if (!isAdmin(request)) {
            return Result.error(403, "权限不足，仅管理员可访问");
        }

        try {
            // 查询所有知识库，按创建时间倒序
            LambdaQueryWrapper<KnowledgeBases> knowledgeQueryWrapper = new LambdaQueryWrapper<>();
            knowledgeQueryWrapper.orderByDesc(KnowledgeBases::getCreateTime);
            List<KnowledgeBases> knowledgeBasesList = knowledgeService.list(knowledgeQueryWrapper);

            // 为每个知识库查询其下属的文档
            List<KnowledgeBaseTreeVO> treeVOList = knowledgeBasesList.stream()
                    .map(knowledgeBase -> {
                        KnowledgeBaseTreeVO vo = KnowledgeBaseTreeVO.fromKnowledgeBase(knowledgeBase);
                        
                        // 查询该知识库下的所有文档，按创建时间倒序
                        LambdaQueryWrapper<Document> documentQueryWrapper = new LambdaQueryWrapper<>();
                        documentQueryWrapper.eq(Document::getBaseId, knowledgeBase.getId())
                                .orderByDesc(Document::getCreateTime);
                        List<Document> documents = documentService.list(documentQueryWrapper);
                        
                        vo.setDocuments(documents);
                        vo.setDocumentCount((long) documents.size());
                        
                        return vo;
                    })
                    .collect(Collectors.toList());

            return Result.success(treeVOList);
        } catch (Exception e) {
            log.error("获取知识库与文档树形结构列表失败", e);
            return Result.error("获取知识库与文档列表失败");
        }
    }

    /**
     * 删除任意文档
     */
    @Operation(summary = "删除任意文档")
    @DeleteMapping("/document/{id}")
    public Result<?> deleteDocument(@PathVariable Integer id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return Result.error(403, "权限不足，仅管理员可访问");
        }

        try {
            Document document = documentService.getById(id);
            if (document == null) {
                return Result.error("文档不存在");
            }

            boolean deleted = documentService.removeById(id);
            if (deleted) {
                log.info("管理员已删除文档 ID: {}, 标题：{}", id, document.getTitle());
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除文档失败，文档 ID: {}", id, e);
            return Result.error("删除失败");
        }
    }

    /**
     * 获取系统分类列表
     */
    @Operation(summary = "获取系统分类列表")
    @GetMapping("/category/system")
    public Result<List<Category>> getSystemCategories(HttpServletRequest request) {
        if (!isAdmin(request)) {
            return Result.error(403, "权限不足，仅管理员可访问");
        }

        try {
            LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Category::getIsSystem, 1)
                    .orderByAsc(Category::getSortOrder);
            List<Category> categories = categoryService.list(queryWrapper);

            return Result.success(categories);
        } catch (Exception e) {
            log.error("获取系统分类失败", e);
            return Result.error("获取系统分类失败");
        }
    }

    /**
     * 创建系统分类
     */
    @Operation(summary = "创建系统分类")
    @PostMapping("/category/system")
    public Result<?> createSystemCategory(@RequestBody Category category, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return Result.error(403, "权限不足，仅管理员可访问");
        }

        try {
            // 检查分类名称是否重复
            LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Category::getName, category.getName())
                    .eq(Category::getIsSystem, 1);
            if (categoryService.count(queryWrapper) > 0) {
                return Result.error("分类名称已存在");
            }

            category.setIsSystem(true);
            boolean saved = categoryService.save(category);

            if (saved) {
                log.info("管理员已创建系统分类：{}", category.getName());
                return Result.success("创建成功");
            } else {
                return Result.error("创建失败");
            }
        } catch (Exception e) {
            log.error("创建系统分类失败", e);
            return Result.error("创建失败");
        }
    }

    /**
     * 删除系统分类
     */
    @Operation(summary = "删除系统分类")
    @DeleteMapping("/category/system/{id}")
    public Result<?> deleteSystemCategory(@PathVariable Integer id, HttpServletRequest request) {
        if (!isAdmin(request)) {
            return Result.error(403, "权限不足，仅管理员可访问");
        }

        try {
            Category category = categoryService.getById(id);
            if (category == null) {
                return Result.error("分类不存在");
            }

            if (!category.getIsSystem()) {
                return Result.error("只能删除系统分类");
            }

            boolean deleted = categoryService.removeById(id);
            if (deleted) {
                log.info("管理员已删除系统分类：{}", category.getName());
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除系统分类失败，分类 ID: {}", id, e);
            return Result.error("删除失败");
        }
    }

    /**
     * 获取统计数据
     */
    @Operation(summary = "获取统计数据")
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics(HttpServletRequest request) {
        if (!isAdmin(request)) {
            return Result.error(403, "权限不足，仅管理员可访问");
        }

        try {
            Map<String, Object> statistics = new HashMap<>();

            // 用户总数
            long totalUsers = userService.count();
            statistics.put("totalUsers", totalUsers);

            // 活跃用户数（最近 7 天登录过）
            // 这里简化处理，实际应该根据 last_login_time 查询
            statistics.put("activeUsers", 0);

            // 文档总数
            long totalDocuments = documentService.count();
            statistics.put("totalDocuments", totalDocuments);

            // 知识库总数
            long totalKnowledgeBases = knowledgeService.count();
            statistics.put("totalKnowledgeBases", totalKnowledgeBases);

            // 分类总数
            long totalCategories = categoryService.count();
            statistics.put("totalCategories", totalCategories);

            // 系统分类数量
            long systemCategories = categoryService.lambdaQuery()
                    .eq(Category::getIsSystem, 1)
                    .count();
            statistics.put("systemCategories", systemCategories);

            // 用户分类数量
            long userCategories = categoryService.lambdaQuery()
                    .eq(Category::getIsSystem, 0)
                    .count();
            statistics.put("userCategories", userCategories);

            log.info("管理员获取了统计数据");
            return Result.success(statistics);
        } catch (Exception e) {
            log.error("获取统计数据失败", e);
            return Result.error("获取统计数据失败");
        }
    }
}
