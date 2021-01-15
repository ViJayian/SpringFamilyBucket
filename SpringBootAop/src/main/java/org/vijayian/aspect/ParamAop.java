package org.vijayian.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.SourceLocation;
import org.springframework.stereotype.Component;
import org.vijayian.entity.Hello;

import java.util.Arrays;

/**
 * aspect.
 *
 * @author ViJay
 */
@Aspect
@Component
public class ParamAop {
    @Pointcut("execution(* org.vijayian.controller.*.*(..))")
    public void pointcut() {

    }

    //>> TODO Caused by: java.lang.IllegalArgumentException: ProceedingJoinPoint is only supported for around advice
    //>> TODO 只能在环绕通知中使用ProceedingJoinPoint
    @Before("pointcut()")
    public void before(/*ProceedingJoinPoint joinPoint*/) {
        System.out.println("before");
    }

    //>> TODO 定义返回值才能将controller的处理结果返回
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("around");
        Object[] args = joinPoint.getArgs();
        System.out.println(String.format("请求参数：%s", Arrays.toString(args)));

        for (Object arg : args) {
            if (arg instanceof String) {
                System.out.println("is String");
            } else if (arg instanceof Hello) {
                Hello hello = (Hello) arg;
                if (!"abc".equals(hello.getName())) {
                    System.out.println("is not valid name");
                } else {
                    System.out.println("is Hello");
                }
            }
        }
        return joinPoint.proceed(args);
    }
}
