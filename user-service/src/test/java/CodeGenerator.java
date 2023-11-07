import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class CodeGenerator {

    @Test
    void name() {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/fastcab", "root", "root")
                .globalConfig(builder -> {
                    builder.author("fancy wu") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件,全局覆盖已有文件的配置已失效，已迁移到策略配置中
                            .outputDir("G:\\Idea\\workspace\\fastcab\\user-service\\src\\main\\java")
                    ; // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.cn.fastcab") // 设置父包名
                            //.moduleName("crud") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "G:\\Idea\\workspace\\fastcab\\user-service\\src\\main\\resources\\mapper"))
                    ; // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("tbl_user_auths") // 设置需要生成的表名
                            .addTablePrefix("tbl") // 设置过滤表前缀
                            .entityBuilder().enableFileOverride() // 覆盖已生成文件
                            .serviceBuilder().enableFileOverride() // 覆盖已生成文件
                            .mapperBuilder().enableFileOverride() // 覆盖已生成文件
                            .controllerBuilder().enableFileOverride()
                    ; // 覆盖已生成文件
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
