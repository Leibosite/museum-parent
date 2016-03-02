package com.qingruan.museum.engine.actor;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import akka.actor.ActorSelection;
import akka.zeromq.Connect;
import akka.zeromq.Listener;
import akka.zeromq.MaxMsgSize;
import akka.zeromq.MulticastLoop;
import akka.zeromq.Rate;
import akka.zeromq.ReceiveBufferSize;
import akka.zeromq.ReceiveHighWatermark;
import akka.zeromq.ReconnectIVL;
import akka.zeromq.ReconnectIVLMax;
import akka.zeromq.SocketOption;
import akka.zeromq.ZMQMessage;
import akka.zeromq.ZeroMQExtension;

import com.qingruan.framework.actor.mvc.ActorSupport;
import com.qingruan.museum.engine.JsonDispatcher;
import com.qingruan.museum.engine.exception.ExceptionLog;
import com.qingruan.museum.framework.util.ExceptionLogUtil;

/**
 * 消息路由Actor.
 *
 * @author tommy
 */
@Slf4j
public abstract class DomainRouteActor extends ActorSupport {

    @Setter
    @Autowired
    protected JsonDispatcher jsonDispatcher;
    @Setter
    protected String zeroMQReceiveEndpoint;

//	private ActorRef testActorRef = this.getContext().system().actorOf(
//			Props.create(TestActor.class).withDispatcher("gx-test-dispatcher"), 
//			"testActor-" + UUID.randomUUID().toString());

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.initZeroMQ();
    }

    protected void initZeroMQ() {
        log.info("Connecting zeromq,  endpoint : {}", zeroMQReceiveEndpoint);
//        if (StringUtils.isNotBlank(zeroMQReceiveEndpoint)) {
//            ZeroMQExtension.get(getContext().system()).newPullSocket(
//                    new SocketOption[] { ZeroMQContextHolder.getContext(),
//                            new Listener(getSelf()),
//                            new Connect(zeroMQReceiveEndpoint),
//                            new ReceiveHighWatermark(10000L), //深水位，按照消息积压3s计算
//                            new MulticastLoop(false),
//                            new Rate(4800000L),	//每秒接收1500条消息，每条消息按照1.6Kb计算 1500 * 1600
//                            new ReceiveBufferSize(20480000L),	//
//                            new MaxMsgSize(-1L),
//                            new ReconnectIVL(1000), new ReconnectIVLMax(8000)
//                           
//                    });
//        }
    }
    protected void initRedisMQ(){
    	 log.info("Connecting mq,  endpoint : {}", zeroMQReceiveEndpoint);
    	
    	
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
        log.error("restart: {}", ExceptionUtils.getFullStackTrace(reason));
        super.postRestart(reason);
    }

    @Override
    public void onReceive(Object msg) {
        if (msg instanceof ZMQMessage) {
            ZMQMessage m = (ZMQMessage) msg;

            String value = extractZmqMsgContent(m);

            log.info("Received message: [{}]", value);

            Object domain = null;

            try {
                domain = jsonDispatcher.jsonToDomain(value);
            } catch (Exception e) {
                log.error("zmq message to json error: {}", value);
                log.error(ExceptionLog.getErrorStack(e));

                return;
            }


            if (domain != null) {
                ActorSelection actorRef = getActorRef(domain);
                if (actorRef != null) {
                    log.info("Send message to deal: {}", value);
                    try{
                        actorRef.tell(domain, getSelf());
                    }catch(Exception e){
                        log.error("send message error: {}", ExceptionLogUtil.getErrorStack(e));
                    }

                    log.info("Finish send message to deal: {}", value);

//					testActorRef.tell(domain, ActorRef.noSender());
//					log.info("Send test value: {}", domain.toString());
                }else{
                    log.warn("No actor found for this zmq message: {}", domain);
                }
            }
        }else{
            log.info("No actor found or message is not ZMQMessage. [{}].", msg);
            unhandled(msg);
        }
    }

    protected abstract ActorSelection getActorRef(Object domain);

    protected abstract String extractZmqMsgContent(ZMQMessage zmqMessage);
}
