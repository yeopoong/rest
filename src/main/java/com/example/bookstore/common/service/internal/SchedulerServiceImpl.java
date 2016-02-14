
package com.example.bookstore.common.service.internal;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.ScheduledMethodRunnable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bookstore.common.domain.Book;
import com.example.bookstore.common.mapper.BookMapper;
import com.example.bookstore.common.service.SchedulerService;

@Service
@Transactional(readOnly = true)
public class SchedulerServiceImpl implements SchedulerService {
	
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired 
	private BookMapper bookMapper;
	
//	@Autowired
//    @Qualifier(value="infScheduler")
//    private TaskScheduler taskScheduler;
	
	private ScheduledTaskRegistrar registrar;

	@Override
    public void reloadSchedule() {
		scheduleJob();
    }
	
	@Override
	public void setScheduledTaskRegistrar(ScheduledTaskRegistrar registrar) {
		this.registrar = registrar;
	}

    public synchronized void scheduleJob() {
//    	registrar.destroy();
    	CronTask[] cronTasks = getCronTasks();

		for (CronTask cronTask : cronTasks) {
			registrar.addCronTask(cronTask);
		}
    }
    
    public CronTask[] getCronTasks() {
		List<Book> bookList = bookMapper.select();
		
		CronTask[] cronTasks = new CronTask[bookList.size()];
		CronTask cronTask = null;
		int index = 0;
		for (Book book : bookList) {
			cronTask = getCronTask(book);
			cronTasks[index++] = cronTask;
		}

		return cronTasks;
	}

	private CronTask getCronTask(Book book) {
		String serviceName = "bookServiceImpl";
		String methodName = "getScheduledBooks";
		String cron = "0 */1 * * * ?"; 

		Runnable runnable = getRunnable(serviceName, methodName);
		CronTask cronTask = createCronTask(runnable, cron);

		return cronTask;
	}

	private Runnable getRunnable(String servicName, String methodName) {
		Object bean = applicationContext.getBean(servicName);
		Method method = getMethod(bean, methodName);

//		Assert.isTrue(void.class.equals(method.getReturnType()),
//				"Only void-returning methods may be annotated with @Scheduled");
//		Assert.isTrue(method.getParameterTypes().length == 0,
//				"Only no-arg methods may be annotated with @Scheduled");
		
		Runnable runnable = new ScheduledMethodRunnable(bean, method);

		return runnable;
	}

	private Method getMethod(Object bean, String methodName) {
		Method method = null;
		try {
			method = bean.getClass().getMethod(methodName);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return method;
	}

	private CronTask createCronTask(Runnable runnable, String cron) {
		CronTask cronTask = null; 

		if (!"".equals(cron)) {
			cronTask = new CronTask(runnable, cron);
		}

		return cronTask;
	}
}
