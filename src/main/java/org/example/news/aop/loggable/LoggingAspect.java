package org.example.news.aop.loggable;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Аспект для аннотации @Loggable.
 * Реализация декларативного базового логирования метода или всех методав класса.
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {
  @Before("@within(Loggable) || @annotation(Loggable)")
  public void logBefore(JoinPoint joinPoint) {
    log.info("Вызываем метод: {}", joinPoint.getSignature().toShortString());
  }

  @After("@within(Loggable) || @annotation(Loggable)")
  public void logAfter(JoinPoint joinPoint) {
    log.info("Завершен метод: {}", joinPoint.getSignature().toShortString());
  }

  @AfterReturning(pointcut = "@within(Loggable) || @annotation(Loggable)", returning = "result")
  public void logAfterReturning(JoinPoint joinPoint, Object result) {
    log.debug(
        "Из метода {} вернулось значение: {}", joinPoint.getSignature().toShortString(), result);
  }

  @AfterThrowing(pointcut = "@within(Loggable) || @annotation(Loggable)", throwing = "exception")
  public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
    log.warn("Исключение в методе: {}", joinPoint.getSignature().toShortString(), exception);
  }

  /**
   * Включает в себя вызов аннотируемого метода.
   *
   * @param proceedingJoinPoint - аннотируемый метод
   * @return - результат выполнения аннотируемого метода
   * @throws Throwable - то, что может выбросить аннотируемый метод
   */
  @Around("@within(Loggable) || @annotation(Loggable)")
  public Object logAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
    log.debug("Счёт времени начат, метод: {}", proceedingJoinPoint.getSignature().toShortString());
    Long start = System.currentTimeMillis();

    Object result = proceedingJoinPoint.proceed();

    Long duration = System.currentTimeMillis() - start;
    log.debug("Счёт времени окончен: {} мс, метод: {}",
        duration, proceedingJoinPoint.getSignature().toShortString());

    return result;
  }
}
