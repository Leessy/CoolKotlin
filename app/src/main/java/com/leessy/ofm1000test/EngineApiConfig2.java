package com.leessy.ofm1000test;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thonnn.cubeengine.engineapi.EngineAsynClient;
import com.thonnn.cubeengine.engineapi.asset.CommandMsgBean;
import com.thonnn.cubeengine.engineapi.asset.EngineCallback;
import com.thonnn.cubeengine.engineapi.entity.ResEntity;
import com.thonnn.cubeengine.engineapi.exception.ConfigFormatException;
import com.thonnn.cubeengine.engineapi.exception.ConfigResetException;
import com.thonnn.cubeengine.engineapi.interfaces.IMqttReceiver;
import com.thonnn.cubeengine.engineapi.utils.MyGsonUtil;
import com.thonnn.cubeengine.engineapi.utils.SoutUtil;
import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.Message;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

public class EngineApiConfig2 {
    private static SharedPreferences sharedPreferences = null;
    private static final String API_VERSION = "0.1.1";
    private static final String URI_INSERT = "/Api/Insert.do";
    private static final String URI_DELETE = "/Api/Delete.do";
    private static final String URI_UPDATE = "/Api/Update.do";
    private static final String URI_SELECT = "/Api/Select.do";
    private static final String URI_UPLOAD = "/Api/Upload.do";
    private static final String URI_GETFILE = "/Api/GetFile.do";
    private static final String URI_DEVICE_INIT = "/Api/Init.do";
    private static final String URI_CMD_FINISH = "/Api/Finish.do";
    private static Context context = null;
    private static String namespace = null;
    private static String deviceName = null;
    private static String deviceType = null;
    private static String deviceSn = null;
    private static String apiHostUri = null;
    private static Boolean useMqtt = null;
    private static String mqttUrl = null;
    private static String mqttUsername = null;
    private static String mqttPasswd = null;
    private static String mqttTopicHead = null;
    private static String mqttClientId = null;
    private static String mqttWillTopic = null;
    private static String mqttWillMsg = null;
    private static Boolean mqttWillRetain = null;
    private static IMqttReceiver mqttReceiver = null;
    private static boolean inited = false;
    private static Thread mqttThread = null;
    private static boolean mqttStopFlag = false;
    private static Gson gson = MyGsonUtil.getGson();

    public EngineApiConfig2() {
    }

    public static String getApiVersion() {
        return "0.1.1";
    }

    public static boolean isInited() {
        return inited;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) throws ConfigResetException {
        if (inited) {
            throw new ConfigResetException("EngineApiConfig2.context");
        } else {
            context = context;
        }
    }

    public static String getNamespace() {
        return namespace;
    }

    public static void setNamespace(String namespace) throws ConfigResetException {
        if (inited) {
            throw new ConfigResetException("EngineApiConfig2.apiHostUri");
        } else {
            namespace = namespace;
        }
    }

    public static String getDeviceName() {
        return deviceName;
    }

    public static void setDeviceName(String deviceName) throws ConfigResetException {
        if (inited) {
            throw new ConfigResetException("EngineApiConfig2.deviceName");
        } else {
            deviceName = deviceName;
        }
    }

    public static String getDeviceType() {
        return deviceType;
    }

    public static void setDeviceType(String deviceType) throws ConfigResetException {
        if (inited) {
            throw new ConfigResetException("EngineApiConfig2.type");
        } else {
            deviceType = deviceType;
        }
    }

    public static String getApiHostUri() {
        return apiHostUri;
    }

    public static void setApiHostUri(String apiHostUri) throws ConfigResetException, ConfigFormatException {
        if (inited) {
            throw new ConfigResetException("EngineApiConfig2.apiHostUri");
        } else if (!apiHostUri.matches("^(http://|https://).*")) {
            throw new ConfigFormatException("EngineApiConfig2.apiHostUri");
        } else {
            apiHostUri = apiHostUri.matches("(.*)/+$") ? apiHostUri.replaceAll("/+$", "") : apiHostUri;
        }
    }

    public static String getDeviceSn() {
        return deviceSn;
    }

    public static void setDeviceSn(String deviceSn) throws ConfigResetException {
        if (inited) {
            throw new ConfigResetException("EngineApiConfig2.deviceSn");
        } else {
            deviceSn = deviceSn;
        }
    }

    public static Boolean getUseMqtt() {
        return useMqtt;
    }

    public static void setUseMqtt(boolean useMqtt) throws ConfigResetException {
        if (inited) {
            throw new ConfigResetException("EngineApiConfig2.useMqtt");
        } else {
            useMqtt = useMqtt;
        }
    }

    public static String getMqttUrl() {
        return mqttUrl;
    }

    public static void setMqttUrl(String mqttUrl) throws ConfigResetException {
        if (inited) {
            throw new ConfigResetException("EngineApiConfig2.mqttUrl");
        } else {
            mqttUrl = mqttUrl;
        }
    }

    public static String getMqttUsername() {
        return mqttUsername;
    }

    public static void setMqttUsername(String mqttUsername) throws ConfigResetException {
        if (inited) {
            throw new ConfigResetException("EngineApiConfig2.mqttUsername");
        } else {
            mqttUsername = mqttUsername;
        }
    }

    public static String getMqttPasswd() {
        return mqttPasswd;
    }

    public static void setMqttPasswd(String mqttPasswd) throws ConfigResetException {
        if (inited) {
            throw new ConfigResetException("EngineApiConfig2.mqttPasswd");
        } else {
            mqttPasswd = mqttPasswd;
        }
    }

    public static String getMqttTopicHead() {
        return mqttTopicHead;
    }

    public static void setMqttTopicHead(String mqttTopicHead) throws ConfigResetException {
        if (inited) {
            throw new ConfigResetException("EngineApiConfig2.mqttTopicHead");
        } else {
            mqttTopicHead = mqttTopicHead;
        }
    }

    public static String getMqttClientId() {
        return mqttClientId;
    }

    public static void setMqttClientId(String mqttClientId) throws ConfigResetException {
        if (inited) {
            throw new ConfigResetException("EngineApiConfig2.mqttClientId");
        } else {
            mqttClientId = mqttClientId;
        }
    }

    public static String getMqttWillTopic() {
        return mqttWillTopic;
    }

    public static void setMqttWillTopic(String mqttWillTopic) throws ConfigResetException {
        if (inited) {
            throw new ConfigResetException("EngineApiConfig2.mqttWillTopic");
        } else {
            mqttWillTopic = mqttWillTopic;
        }
    }

    public static String getMqttWillMsg() {
        return mqttWillMsg;
    }

    public static void setMqttWillMsg(String mqttWillMsg) throws ConfigResetException {
        if (inited) {
            throw new ConfigResetException("EngineApiConfig2.mqttWillMsg");
        } else {
            mqttWillMsg = mqttWillMsg;
        }
    }

    public static boolean getMqttWillRetain() {
        return mqttWillRetain;
    }

    public static void setMqttWillRetain(boolean mqttWillRetain) throws ConfigResetException {
        if (inited) {
            throw new ConfigResetException("EngineApiConfig2.mqttWillRetain");
        } else {
            mqttWillRetain = mqttWillRetain;
        }
    }

    public static IMqttReceiver getMqttReceiver() {
        return mqttReceiver;
    }

    public static void setMqttReceiver(IMqttReceiver mqttReceiver) throws ConfigResetException {
        if (inited) {
            throw new ConfigResetException("EngineApiConfig2.mqttReceiver");
        } else {
            mqttReceiver = mqttReceiver;
        }
    }

    public static String getUrl_Insert() {
        return apiHostUri + "/Api/Insert.do";
    }

    public static String getUrl_Delete() {
        return apiHostUri + "/Api/Delete.do";
    }

    public static String getUrl_Update() {
        return apiHostUri + "/Api/Update.do";
    }

    public static String getUrl_Select() {
        return apiHostUri + "/Api/Select.do";
    }

    public static String getUrl_Upload() {
        return apiHostUri + "/Api/Upload.do";
    }

    public static String getUrl_GetFile() {
        return apiHostUri + "/Api/GetFile.do";
    }

    public static String getUrl_DeviceInit() {
        return apiHostUri + "/Api/Init.do";
    }

    public static String getUrl_CmdFinish() {
        return apiHostUri + "/Api/Finish.do";
    }

    public static boolean init() throws NullPointerException, ConfigResetException {
        SoutUtil.log("CubeEngineApi对接引擎初始化 开始");
        if (inited) {
            throw new ConfigResetException("EngineApiConfig2.init()");
        } else if (namespace == null) {
            throw new NullPointerException("EngineApiConfig2.namespace");
        } else if (deviceName == null) {
            throw new NullPointerException("EngineApiConfig2.deviceName");
        } else if (deviceType == null) {
            throw new NullPointerException("EngineApiConfig2.deviceType");
        } else if (apiHostUri == null) {
            throw new NullPointerException("EngineApiConfig2.apiHostUri");
        } else if (deviceSn == null) {
            throw new NullPointerException("EngineApiConfig2.deviceSn");
        } else if (useMqtt == null) {
            throw new NullPointerException("EngineApiConfig2.useMqtt");
        } else {
            if (useMqtt) {
                if (context == null) {
                    throw new NullPointerException("EngineApiConfig2.context");
                }

                if (mqttUrl == null) {
                    throw new NullPointerException("EngineApiConfig2.mqttUrl");
                }

                if (mqttUsername == null) {
                    throw new NullPointerException("EngineApiConfig2.mqttUsername");
                }

                if (mqttPasswd == null) {
                    throw new NullPointerException("EngineApiConfig2.mqttPasswd");
                }

                if (mqttTopicHead == null) {
                    throw new NullPointerException("EngineApiConfig2.mqttTopicHead");
                }

                if (mqttClientId == null) {
                    throw new NullPointerException("EngineApiConfig2.mqttClientId");
                }

                if (mqttWillTopic == null) {
                    throw new NullPointerException("EngineApiConfig2.mqttWillTopic");
                }

                if (mqttWillMsg == null) {
                    throw new NullPointerException("EngineApiConfig2.mqttWillMsg");
                }

                if (mqttWillRetain == null) {
                    throw new NullPointerException("EngineApiConfig2.mqttWillRetain");
                }

                if (mqttReceiver == null) {
                    throw new NullPointerException("EngineApiConfig2.mqttReceiver");
                }

                sharedPreferences = context.getSharedPreferences("engineMqttLog", 0);
            }

            mqttThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        EngineAsynClient.getClient().init(new EngineCallback<Void>((new TypeToken<ResEntity<Void>>() {
                        }).getType()) {
                            public void callback(ResEntity<Void> resEntity, String resJson) {
                                super.callback(resEntity, resJson);
                                SoutUtil.log("CubeEngineApi对接引擎初始化 结束，请求结果：" + resEntity.getErrmsg());
                            }
                        });
                        if (EngineApiConfig2.useMqtt) {
                            SoutUtil.log("正在启动 MQTT 指令通道");
                            MQTT mqtt = new MQTT();
                            mqtt.setHost(EngineApiConfig2.mqttUrl);
                            mqtt.setUserName(EngineApiConfig2.mqttUsername);
                            mqtt.setPassword(EngineApiConfig2.mqttPasswd);
                            mqtt.setClientId(EngineApiConfig2.mqttClientId);
                            mqtt.setWillTopic(EngineApiConfig2.mqttWillTopic);
                            mqtt.setWillMessage(EngineApiConfig2.mqttWillMsg);
                            mqtt.setWillRetain(EngineApiConfig2.mqttWillRetain);
                            BlockingConnection conn = mqtt.blockingConnection();
                            conn.connect();
                            Topic[] topics = new Topic[]{new Topic(EngineApiConfig2.mqttTopicHead + "_" + EngineApiConfig2.deviceSn, QoS.EXACTLY_ONCE)};
                            conn.subscribe(topics);
                            SoutUtil.log("EngineApi: MQTT 连接成功！");

                            while(!EngineApiConfig2.mqttStopFlag) {
                                Message message = conn.receive();
                                byte[] payload = message.getPayload();
                                message.ack();
                                String msg = new String(payload);
                                final CommandMsgBean commandMsgBean = (CommandMsgBean) EngineApiConfig2.gson.fromJson(msg, CommandMsgBean.class);
                                commandMsgBean.serialize();
                                int lastCmdMsgId = EngineApiConfig2.sharedPreferences.getInt("LastCmdMsgId", -1);
                                if (commandMsgBean.getCmid() == lastCmdMsgId) {
                                    SoutUtil.log("EngineClient MQTT 忽略曾接收过的指令：" + lastCmdMsgId);
                                } else {
                                    Editor editor = EngineApiConfig2.sharedPreferences.edit();
                                    editor.putInt("LastCmdMsgId", commandMsgBean.getCmid());
                                    editor.apply();
                                    if (EngineApiConfig2.mqttReceiver != null) {
                                        final String cmdRsl = EngineApiConfig2.mqttReceiver.callback(commandMsgBean);
                                        if (cmdRsl != null) {
                                            SoutUtil.warn("cmdRsl 不为 null，自动发送 任务完成 信息");
                                            EngineAsynClient.getClient().finish(commandMsgBean.getCmid(), cmdRsl, new EngineCallback<Void>((new TypeToken<ResEntity<Void>>() {
                                            }).getType()) {
                                                public void callback(ResEntity<Void> resEntity, String resJson) {
                                                    super.callback(resEntity, resJson);
                                                    SoutUtil.log("完成任务自动请求CubeEngine, CMID: " + commandMsgBean.getCmid() + ", 附加结果：" + cmdRsl + ", 请求结果：" + resJson);
                                                }
                                            });
                                        }
                                    }

                                    Thread.sleep(1000L);
                                }
                            }
                        }
                    } catch (Exception var11) {
                        var11.printStackTrace();
                    }

                }
            });
            inited = true;
            mqttThread.start();
            return true;
        }
    }

    public static void stopMqtt() {
        mqttStopFlag = true;
        mqttThread.interrupt();
    }
}
