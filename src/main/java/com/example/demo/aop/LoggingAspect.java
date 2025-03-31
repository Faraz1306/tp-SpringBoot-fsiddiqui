package com.example.demo.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // Pointcut sur les méthodes de la couche service
    @Pointcut("within(com.example.demo.service..*)")
    public void serviceLayer() {}

    @Before("serviceLayer()")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Début de l'exécution de : " + joinPoint.getSignature().toShortString());
    }

    @AfterReturning(pointcut = "serviceLayer()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        logger.info("Fin de l'exécution de : " + joinPoint.getSignature().toShortString() + " - Résultat : " + result);
    }
}
