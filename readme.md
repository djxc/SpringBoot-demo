在application.properties中配置静态资源访问地址，返回html页面需要在控制器加@Controller标签，而不是@RestController
加载postgresql数据库驱动库，用以操作pg数据库

2.实体类(domain)置于com.dj.springboot.domain

3.数据访问层(Dao)置于com.dj.springboot.Dao

4.数据服务层(Service)置于com,springboot.service,数据服务的实现接口(serviceImpl)至于com.springboot.service.impl

5.前端控制器(Controller)置于com.dj.springboot.Controller

6.工具类(utils)置于com.dj.springboot.Utils

7.常量接口类(constant)置于com.dj.springboot.Constant

8.配置信息类(config)置于com.springboot.config

9.数据传输类(vo)置于com.springboot.vo