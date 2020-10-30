package test.baseapp.co.id.common.exception.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import test.baseapp.co.id.common.stereotype.ValueProvider;
import test.baseapp.co.id.common.stereotype.exception.ExceptionType;
import test.baseapp.co.id.common.stereotype.exception.Log;
import test.baseapp.co.id.common.stereotype.exception.LogDetail;
import test.baseapp.co.id.common.stereotype.exception.RootCause;
import test.baseapp.co.id.common.stereotype.exception.RootCauseDetail;
import test.baseapp.co.id.common.stereotype.exception.repo.LogDetailRepo;
import test.baseapp.co.id.common.stereotype.exception.repo.LogRepo;
import test.baseapp.co.id.common.stereotype.exception.repo.RootCauseDetailRepo;
import test.baseapp.co.id.common.stereotype.exception.repo.RootCauseRepo;
import test.baseapp.co.id.common.stereotype.security.User;
import test.baseapp.co.id.common.stereotype.security.repo.UserRepo;

@SuppressWarnings({"rawtypes", "unchecked"})
@Service
public class ExceptionService {
	
	@Value("${app.name}")
	private String appName;
	
	@Value("${app.version}")
	private String appVersion;
	
	@Autowired
	private LogRepo logRepo;
	
	@Autowired
	private LogDetailRepo logDetailRepo;
	
	@Autowired
	private RootCauseRepo rootCauseRepo;
	
	@Autowired
	private RootCauseDetailRepo rootCauseDetailRepo;
	
	private Class<Log> logClass;
	
	private Class<LogDetail> logDetailClass;
	
	private Class<RootCause> rootCauseClass;
	
	private Class<RootCauseDetail> rootCauseDetailClass;
	
	@Autowired
	private UserRepo userRepo;
	
	@PostConstruct
	private void init() {
		logClass = (Class<Log>) ResolvableType.forInstance(logRepo).as(LogRepo.class).getGeneric(0).resolve();
		logDetailClass = (Class<LogDetail>) ResolvableType.forInstance(logDetailRepo).as(LogDetailRepo.class).getGeneric(0).resolve();
		rootCauseClass = (Class<RootCause>) ResolvableType.forInstance(rootCauseRepo).as(RootCauseRepo.class).getGeneric(0).resolve();
		rootCauseDetailClass = (Class<RootCauseDetail>) ResolvableType.forInstance(rootCauseDetailRepo).as(RootCauseDetailRepo.class).getGeneric(0).resolve();
	}
	
	@Transactional
	public Log saveExceptionLog(Throwable throwable, ExceptionType type, String userId) throws Exception {
		User user = getUser(userId);
		Log log = setException(throwable, type);
		if(user != null)
			log.setUser(user);
		LogDetail logDetail = setExceptionDetail(log, throwable);
		RootCause rootCause = setRootCause(log, throwable);
		RootCauseDetail rootCauseDetail = setRootCauseDetail(throwable, rootCause);
		
		return saveException(log, logDetail, rootCause, rootCauseDetail);
	}
	
	@Transactional
	public Log saveException(Log log, LogDetail logDetail, RootCause rootCause, RootCauseDetail rootCauseDetail, String userId) {
		User user = getUser(userId);
		if(user != null)
			log.setUser(user);
		log = (Log) logRepo.save(log);
		logDetail.setExceptionLog(log);
		rootCause.setExceptionLog(log);
		rootCause = (RootCause) rootCauseRepo.save(rootCause);
		rootCauseDetail.setRootCause(rootCause);
		saveException(log, logDetail, rootCause,rootCauseDetail);
		return log;
	}
	
	private Log saveException(Log log, LogDetail logDetail, RootCause rootCause, RootCauseDetail rootCauseDetail) {
		log = (Log) logRepo.save(log);
		logDetailRepo.save(logDetail);
		rootCauseRepo.save(rootCause);
		rootCauseDetailRepo.save(rootCauseDetail);
		return log;
	}
	
	private User getUser(String userId) {
		User user = null;
		if(userId != null) {
			user = (User) userRepo.findOneByUsernameIgnoreCase(userId).orElse(null);
		}
		return user;
	}
	
	
	private Log setException(Throwable throwable,ExceptionType type) throws Exception {
		Log log = logClass.newInstance();
		log.setDate(new Date());
		log.setExceptionClass(throwable.getClass().getSimpleName());
		log.setMessage(throwable.getMessage());
		log.setApplicationName(appName);
		log.setApplicationVersion(appVersion);
		log.setType(type);
		return log;
	}
	
	private LogDetail setExceptionDetail(Log log, Throwable throwable) throws Exception{
		LogDetail logDetail = logDetailClass.newInstance();
		StackTraceElement ste = Arrays.asList(throwable.getStackTrace()).stream().findFirst().get();
		logDetail.setExceptionLog(log);
		logDetail.setClassName(ste.getClassName());
		logDetail.setFileName(ste.getFileName());
		logDetail.setMethodName(ste.getMethodName());
		logDetail.setLineNumber(ste.getLineNumber());
		return logDetail;
	}
	
	private RootCause setRootCause(Log log, Throwable throwable) throws Exception {
		RootCause rootCause = rootCauseClass.newInstance();
		Throwable rc = ExceptionUtils.getRootCause(throwable);
		rootCause.setExceptionLog(log);
		rootCause.setExceptionClass(rc.getClass().getSimpleName());
		rootCause.setMessage(rc.getMessage());
		rootCause.setStackTrace(ExceptionUtils.getStackTrace(rc));
		return rootCause;
	}
	
	private RootCauseDetail setRootCauseDetail(Throwable throwable, RootCause rootCause) throws Exception {
		Throwable rc = ExceptionUtils.getRootCause(throwable);
		StackTraceElement ste = Arrays.asList(rc.getStackTrace()).stream().findFirst().orElse(null);
		RootCauseDetail rcd = rootCauseDetailClass.newInstance();
		rcd.setRootCause(rootCause);
		rcd.setClassName(ste.getClassName());
		rcd.setFileName(ste.getFileName());
		rcd.setMethodName(ste.getMethodName());
		rcd.setLineNumber(ste.getLineNumber());
		return rcd;
	}

}
