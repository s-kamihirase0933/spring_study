package com.example.demo.login.aspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
//ポイント1:@Aspect
@Aspect
@Component
public class LogAspect{
	
	//ポイント２：AOPの実装
	@Around("@within(org.springframework.stereotype.Controller)") //PointCut（実行場所） ： @within
	
	public Object startLog(ProceedingJoinPoint jp) throws Throwable{
		System.out.println("メソッド開始：" + jp.getSignature());
		
		try {
			//メソッド実行
			Object result = jp.proceed();
			
			System.out.println("メソッド終了：" + jp.getSignature());
			
			return result;
		} catch(Exception e) {
			System.out.println("メソッド異常終了：" + jp.getSignature());
			e.printStackTrace();
			throw e;
		}
	}
	
	//UserDaoクラスのログ出力
	@Around("execution(* *..*.*UserDao*.*(..))")
	public Object daoLog(ProceedingJoinPoint jp)throws Throwable{
		System.out.println("メソッド開始：" + jp.getSignature());
		
		try {
			Object result = jp.proceed();
			
			System.out.println("メソッド終了：" + jp.getSignature());
			
			return result;
		}catch(Exception e) {
			System.out.println("メソッド異常終了：" + jp.getSignature());
			e.printStackTrace();
			throw e;
		}
	}
	
	@ControllerAdvice
	public class GlobalExceptionHandler {

	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<String> handleException(Exception e) {
	        // ログ出力（必要に応じて logger 使用）
	        e.printStackTrace(); 
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("サーバーエラーが発生しました");
	    }
	}

}
