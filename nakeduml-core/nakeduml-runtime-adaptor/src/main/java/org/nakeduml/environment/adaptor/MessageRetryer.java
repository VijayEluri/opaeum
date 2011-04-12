package org.nakeduml.environment.adaptor;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.InitialContext;

import org.nakeduml.runtime.domain.ExceptionAnalyser;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class MessageRetryer{
	@Resource
	TimerService timerService;
	@Resource(mappedName="ConnectionFactory")
	ConnectionFactory factory;
	public void retryMessage(String destination,Serializable payload){
		long delay = Math.round(Math.random() * 10000);// Randomly spread delay to minimise risk of concurrency
		timerService.createTimer(new Date(System.currentTimeMillis() + delay), new PayloadToResend(destination, payload));
	}
	public static void main(String[] args){
		System.out.println(Math.random());
	}
	@Timeout
	public void redeliver(Timer t){
		PayloadToResend p = (PayloadToResend) t.getInfo();
		Connection conn = null;
		Session sess = null;
		try{
			Queue q = (Queue) new InitialContext().lookup(p.getDestination());
			conn = factory.createConnection();
			sess = conn.createSession(false, Session.CLIENT_ACKNOWLEDGE);
			MessageProducer prod = sess.createProducer(q);
			prod.send(sess.createObjectMessage(p.getPayload()));
			prod.close();
		}catch(RuntimeException e){
			throw e;
		}catch(Exception e){
			new ExceptionAnalyser(e).throwRootCause();
		}finally{
			if(sess != null){
				try{
					sess.close();
				}catch(Exception e){
				}
			}
			if(conn != null){
				try{
					conn.close();
				}catch(Exception e){
				}
			}
		}
	}
}