package ${package}.rest;

import com.github.aop.log.Log;
import ${package}.domain.${className};
import ${package}.service.${className}Service;
import ${package}.service.dto.${className}QueryCriteria;
import com.github.utils.AuthorizationUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
* @author ${author}
* @date ${date}
*/
@Api(tags = "实例：${className}管理")
@RestController
@RequestMapping("/api/${changeClassName?lower_case}")
public class ${className}Controller {

    private final ${className}Service ${changeClassName}Service;

    public ${className}Controller(${className}Service ${changeClassName}Service) {
        this.${changeClassName}Service = ${changeClassName}Service;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
        public void download(HttpServletResponse response, ${className}QueryCriteria criteria) throws IOException {
        ${changeClassName}Service.download(${changeClassName}Service.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询${className}")
    @ApiOperation("查询${className}")
        public ResponseEntity get${className}s(${className}QueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(${changeClassName}Service.queryAll(criteria,pageable),HttpStatus.OK);
    }
	
    @GetMapping(value = "/load/{${pkChangeColName}}")
    @Log("装载${className}")
    @ApiOperation("装载${className}")
        public ResponseEntity load(@PathVariable ${pkColumnType} ${pkChangeColName}){
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping
    @Log("新增${className}")
    @ApiOperation("新增${className}")
        public ResponseEntity create(@Validated @RequestBody ${className} resources, HttpServletRequest request){
		String profileid = AuthorizationUtils.getProfileid(request);
		resources.setAuthor(profileid);
		resources.setId(1000L);
        return new ResponseEntity<>(${changeClassName}Service.create(resources),HttpStatus.CREATED);
    }

    @PutMapping(value = "/{${pkChangeColName}}")
    @Log("修改${className}")
    @ApiOperation("修改${className}")
        public ResponseEntity update(@PathVariable ${pkColumnType} ${pkChangeColName},@Validated @RequestBody ${className} resources){
        ${changeClassName}Service.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
	
    @DeleteMapping(value = "/{${pkChangeColName}}")
    @Log("逻辑删除${className}")
    @ApiOperation("逻辑删除${className}")
        public ResponseEntity delete(@PathVariable ${pkColumnType} ${pkChangeColName}){
        ${changeClassName}Service.delete(${pkChangeColName});
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{${pkChangeColName}}")
    @Log("物理删除${className}")
    @ApiOperation("物理删除${className}")
        public ResponseEntity fulldelete(@PathVariable ${pkColumnType} ${pkChangeColName}){
        ${changeClassName}Service.delete(${pkChangeColName});
        return new ResponseEntity(HttpStatus.OK);
    }
}