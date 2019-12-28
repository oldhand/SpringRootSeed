package com.github.profile.rest;

import cn.hutool.core.util.IdUtil;
import com.github.aop.log.Log;
import com.github.exception.BadRequestException;
import com.github.profile.domain.*;
import com.github.profile.service.ProfileService;
import com.github.profile.service.dto.ProfileDTO;
import com.github.profile.service.dto.ProfileQueryCriteria;
import com.github.profile.service.utils.ProfileUtils;
import com.github.utils.redisUtils;
import com.wf.captcha.ArithmeticCaptcha;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author oldhand
* @date 2019-12-27
*/
@Api(tags = "数据库中间件：用户管理")
@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    @Value("${jwt.codeKey}")
    private String codeKey;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Log("导出用户数据")
    @ApiOperation("导出用户数据")
    @GetMapping(value = "/download")
        public void download(HttpServletResponse response, ProfileQueryCriteria criteria) throws IOException {
        profileService.download(profileService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询用户")
    @ApiOperation("查询用户")
        public ResponseEntity getProfiles(ProfileQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(profileService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    @Log("注册用户")
    @ApiOperation("注册用户")
        public ResponseEntity create(@Validated @RequestBody RegisterProfile resources){
        ProfileDTO profile = profileService.create(resources);
        return new ResponseEntity<>(profile,HttpStatus.CREATED);
    }

    @PostMapping(value = "/login")
    @Log("登录")
    @ApiOperation("登录")
    public ResponseEntity login(@Validated LoginProfile loginprofile){

        // 查询验证码
        String code = redisUtils.getCodeVal(loginprofile.getUuid());
        // 清除验证码
        redisUtils.delete(loginprofile.getUuid());

        if (StringUtils.isBlank(code)) {
            throw new BadRequestException("验证码已过期");
        }
        if (StringUtils.isBlank(loginprofile.getVerifycode()) || !loginprofile.getVerifycode().equalsIgnoreCase(code)) {
            throw new BadRequestException("验证码错误");
        }

        //return new ResponseEntity<>(profileService.create(resources),HttpStatus.CREATED);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/logout/{id}")
    @Log("注销")
    @ApiOperation("注销")
    public ResponseEntity logout(@PathVariable String id){
        //return new ResponseEntity<>(profileService.create(resources),HttpStatus.CREATED);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping
    @Log("修改用户")
    @ApiOperation("修改用户")
        public ResponseEntity update(@Validated @RequestBody UpdateProfile resources){
        profileService.update(resources);
        ProfileDTO profile = profileService.findByUsername(resources.getUsername());
        return new ResponseEntity(profile,HttpStatus.OK);
    }
    @PutMapping(value = "/password")
    @Log("修改密码")
    @ApiOperation("修改密码")
    public ResponseEntity changePassword(@Validated @RequestBody ChangePassword resources){
        //profileService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/disable/{id}")
    @Log("禁止用户")
    @ApiOperation("禁止用户")
    public ResponseEntity disable(@PathVariable String id){
        profileService.disable(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }

    @PostMapping(value = "/enable/{id}")
    @Log("启用用户")
    @ApiOperation("启用用户")
        public ResponseEntity enable(@PathVariable String id){
        profileService.enable(id);
        return new ResponseEntity("ok",HttpStatus.OK);
    }

    @Log("获取验证码")
    @ApiOperation("获取验证码")
    @GetMapping(value = "/verifycode")
    public ImgResult getCode(){
        // 算术类型 https://gitee.com/whvse/EasyCaptcha
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(111, 36);
        // 几位数运算，默认是两位
        captcha.setLen(2);
        // 获取运算的结果：5
        String result = captcha.text();
        String uuid = codeKey + "::" + IdUtil.simpleUUID();
        redisUtils.saveCode(uuid,result);
        return new ImgResult(captcha.toBase64(),uuid);
    }
}