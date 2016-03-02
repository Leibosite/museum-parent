package com.qingruan.museum.engine.actor;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import scala.Option;
import akka.actor.ActorRef;
import akka.util.ByteString;
import akka.zeromq.Connect;
import akka.zeromq.MulticastLoop;
import akka.zeromq.Rate;
import akka.zeromq.ReconnectIVL;
import akka.zeromq.ReconnectIVLMax;
import akka.zeromq.SendBufferSize;
import akka.zeromq.SendHighWatermark;
import akka.zeromq.SocketOption;
import akka.zeromq.ZMQMessage;
import akka.zeromq.ZeroMQExtension;

import com.qingruan.framework.actor.mvc.ActorSupport;
import com.qingruan.framework.actor.spring.annotation.ActorComponent;
import com.qingruan.museum.engine.JsonDispatcher;
import com.qingruan.museum.framework.util.ExceptionLogUtil;

/**
 *
 * @author tommy
 */
@ActorComponent
@Slf4j
public class DomainSendActor extends ActorSupport {
    @Setter
    @Autowired
    private JsonDispatcher jsonDispatcher;

    @Setter
    private String zeroMQSendEndpoint;
    private ActorRef pushActor;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        log.info("Connecting zeromq {}", zeroMQSendEndpoint);
//        if (StringUtils.isNotBlank(zeroMQSendEndpoint)) {
//            pushActor = ZeroMQExtension.get(getContext().system())
//                    .newPushSocket(
//                            new SocketOption[] {
//                                    ZeroMQContextHolder.getContext(),
//                                    new Connect(zeroMQSendEndpoint),
//                                    new SendBufferSize(10240000L),	//缓存大小
//                                    new SendHighWatermark(6000L),
//                                    new Rate(4800000L),	//每秒发送3000条消息，每条消息按照1.6KB计算
//                                    new MulticastLoop(false),
//                                    new ReconnectIVL(1000L),
//                                    new ReconnectIVLMax(8000L)
//                                    // ReconnectIVL & ReconnectIVLMax。
//                            });
//        }
    }

    @Override
    public void postRestart(Throwable reason) throws Exception {
        log.error("postRestart: {}", ExceptionUtils.getFullStackTrace(reason));
        super.postRestart(reason);
    }

    @Override
    public void preRestart(Throwable reason, Option<Object> message)
            throws Exception {
        log.error("preRestart: {}", ExceptionUtils.getFullStackTrace(reason));
        super.preRestart(reason, message);
    }

    @Override
    public void onReceive(Object domain) {
        if (pushActor == null) {
            log.error("No pushActor is configurated.");
            return;
        }

        if (domain == null) {
            log.warn("The message to send is null");
            return;
        }

        String json = null;
        try {
            json = jsonDispatcher.domainToJson(domain);
        } catch (Exception e) {
            log.error(ExceptionLogUtil.getErrorStack(e));
        }

        if (StringUtils.isNotBlank(json)) {
            ZMQMessage message = ZMQMessage.withFrames(ByteString
                    .fromString(json));
            try {
                pushActor.tell(message, getSelf());
            } catch (Exception e) {
                log.error("send massage exception: {}",
                        ExceptionLogUtil.getErrorStack(e));
            }

            log.info("Send back message [{}].", json);
        } else {
            log.error("The json message from domain is null!");
            //unhandled(domain);
        }
    }

}
