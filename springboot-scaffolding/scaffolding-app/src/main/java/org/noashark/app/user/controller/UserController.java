package org.noashark.app.user.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.noashark.app.user.pojo.dto.UserDTO;
import org.noashark.app.user.pojo.query.UserQuery;
import org.noashark.app.user.pojo.req.UserReq;
import org.noashark.app.user.pojo.vo.UserVO;
import org.noashark.app.user.service.IUserService;
import org.noashark.common.pojo.Response;
import org.noashark.common.pojo.dto.PageDTO;
import org.noashark.common.pojo.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping("")
    @ApiOperation("添加用户")
    @Transactional
    public Response<UserDTO> add(@RequestBody @Validated UserDTO userDTO) {

        return Response.ok(null);
    }

    @PostMapping("/{id}")
    @ApiOperation("根据id查询用户")
    @Transactional
    public Response<UserDTO> add(@PathVariable Long id) {

        return Response.ok(null);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("根据ID删除用户")
    public Response<Void> delete(@PathVariable Long id) {

        return Response.ok(null);
    }

    @PatchMapping("/modify")
    @ApiOperation("根据ID修改用户")
    public Response<Void> modify(@RequestBody @Validated UserReq req) {
        return Response.ok(null);
    }

    @PostMapping("/list")
    @ApiOperation("分页查询用户列表")
    public Response<PageVO<UserVO>> list(@RequestBody @Validated PageDTO<UserQuery> pageDTO) {
        PageVO<UserVO> pageVO = userService.voList(pageDTO);
        return Response.ok(pageVO);
    }

}