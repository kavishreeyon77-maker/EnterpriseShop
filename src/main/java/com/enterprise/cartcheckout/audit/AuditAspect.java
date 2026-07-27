package com.enterprise.cartcheckout.audit;

import com.enterprise.cartcheckout.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditService auditService;

    @Around("execution(* com.enterprise.cartcheckout.service.impl.*.*(..))")
    public Object auditServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        long startTime = System.currentTimeMillis();
        
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error("Exception in {}.{}() with cause = {}", className, methodName, e.getMessage() != null ? e.getMessage() : "NULL");
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            if (log.isDebugEnabled()) {
                log.debug("Executed {}.{}() in {}ms", className, methodName, duration);
            }
            
            // Log specific critical actions to DB audit trail
            if (methodName.startsWith("create") || methodName.startsWith("update") || 
                methodName.startsWith("delete") || methodName.startsWith("cancel") || 
                methodName.startsWith("process") || methodName.startsWith("refund")) {
                
                String userId = getCurrentUsername();
                String details = "Method: " + methodName + ", Args: " + Arrays.toString(joinPoint.getArgs());
                
                auditService.logAction(
                    userId,
                    methodName,
                    className.replace("ServiceImpl", ""),
                    "N/A", // Entity ID can be extracted if needed
                    details,
                    "Internal"
                );
            }
        }
        
        return result;
    }
    
    private String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            return auth.getName();
        }
        return "SYSTEM";
    }
}
