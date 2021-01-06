//
//  RTCAudioliveRoomManager.m
//  LectureHall
//
//  Created by Aliyun on 2020/6/15.
//  Copyright © 2020 alibaba. All rights reserved.
//

#import "RTCSmallClass.h"
#import "SmallClassFetcherFactory.h"
#import <CommonCrypto/CommonHMAC.h>

@class RTCEngineDelegate;

/// AliRtcEngineDelegate 的桥接代理类
@protocol RTCEngineBridgeDelegate <NSObject>

- (void)engine:(AliRtcEngine *)engine onBye:(int)code;

- (void)engine:(AliRtcEngine *)engine onOccurError:(int)error;

- (void)engine:(AliRtcEngine *)engine onOccurWarning:(int)warn;

- (void)engine:(AliRtcEngine *)engine onLeaveChannelResult:(int)result;

- (void)engine:(AliRtcEngine *)engine onJoinChannelResult:(int)result authInfo:(AliRtcAuthInfo *)authInfo;

- (void)engine:(AliRtcEngine *)engine onRemoteUserOnLineNotify:(NSString *)uid;

- (void)engine:(AliRtcEngine *)engine onRemoteUserOffLineNotify:(NSString *)uid;

- (void)engine:(AliRtcEngine *)engine onRemoteTrackAvailableNotify:(NSString *)uid audioTrack:(AliRtcAudioTrack)audioTrack videoTrack:(AliRtcVideoTrack)videoTrack;

- (void)engine:(AliRtcEngine *)engine onSubscribeChangedNotify:(NSString *)uid audioTrack:(AliRtcAudioTrack)audioTrack videoTrack:(AliRtcVideoTrack)videoTrack;

- (void)engine:(AliRtcEngine *)engine onAudioVolumeCallback:(NSArray <AliRtcUserVolumeInfo *> *)array totalVolume:(int)totalVolume;

- (void)engine:(AliRtcEngine *)engine onUserAudioMuted:(NSString *)uid audioMuted:(BOOL)isMute;

- (void)engine:(AliRtcEngine *)engine onUserVideoMuted:(NSString *)uid videoMuted:(BOOL)isMute;

- (void)engine:(AliRtcEngine *)engine onNetworkQualityChanged:(NSString *)uid
                                             upNetworkQuality:(AliRtcNetworkQuality)upQuality
                                            downNetworkQuality:(AliRtcNetworkQuality)downQuality;

@end


@interface RTCEngineDelegate : NSObject <AliRtcEngineDelegate>

@property (nonatomic, weak) id<RTCEngineBridgeDelegate> delegate;

@property (nonatomic, weak) AliRtcEngine *engine;

- (instancetype)initWithDelegate:(id<RTCEngineBridgeDelegate>)delegate;

@end

@implementation RTCEngineDelegate

- (instancetype)initWithDelegate:(id<RTCEngineBridgeDelegate>)delegate {
    self = [super init];
    if (self) {
        self.delegate = delegate;
    }
    return self;
}

- (void)onBye:(int)code {
    if ([self.delegate respondsToSelector:@selector(engine:onBye:)]) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.delegate engine:self.engine onBye:code];
        });
    }
}


- (void)onOccurError:(int)error {
    if ([self.delegate respondsToSelector:@selector(engine:onOccurError:)]) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.delegate engine:self.engine onOccurError:error];
        });
        
    }
}

- (void)onOccurWarning:(int)warn {
    if ([self.delegate respondsToSelector:@selector(engine:onOccurWarning:)]) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.delegate engine:self.engine onOccurWarning:warn];
        });
    }
}

- (void)onLeaveChannelResult:(int)result {
    if ([self.delegate respondsToSelector:@selector(engine:onLeaveChannelResult:)]) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.delegate engine:self.engine onLeaveChannelResult:result];
        });
        
    }
}

- (void)onJoinChannelResult:(int)result authInfo:(AliRtcAuthInfo *)authInfo {
    if ([self.delegate respondsToSelector:@selector(engine:onJoinChannelResult:authInfo:)]) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.delegate engine:self.engine onJoinChannelResult:result authInfo:authInfo];
        });
    }
}

- (void)onRemoteUserOnLineNotify:(NSString *)uid {
    if ([self.delegate respondsToSelector:@selector(engine:onRemoteUserOnLineNotify:)]) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.delegate engine:self.engine onRemoteUserOnLineNotify:uid];
        });
        
    }
}

- (void)onRemoteUserOffLineNotify:(NSString *)uid {
    if ([self.delegate respondsToSelector:@selector(engine:onRemoteUserOffLineNotify:)]) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.delegate engine:self.engine onRemoteUserOffLineNotify:uid];
        });
    }
}

- (void)onRemoteTrackAvailableNotify:(NSString *)uid audioTrack:(AliRtcAudioTrack)audioTrack videoTrack:(AliRtcVideoTrack)videoTrack {
    if ([self.delegate respondsToSelector:@selector(engine:onRemoteTrackAvailableNotify:audioTrack:videoTrack:)]) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.delegate engine:self.engine onRemoteTrackAvailableNotify:uid audioTrack:audioTrack videoTrack:videoTrack];
        });
        
    }
}

- (void)onSubscribeChangedNotify:(NSString *)uid audioTrack:(AliRtcAudioTrack)audioTrack videoTrack:(AliRtcVideoTrack)videoTrack {
    if ([self.delegate respondsToSelector:@selector(engine:onSubscribeChangedNotify:audioTrack:videoTrack:)]) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.delegate engine:self.engine onSubscribeChangedNotify:uid audioTrack:audioTrack videoTrack:videoTrack];
        });
    }
}

- (void)onAudioVolumeCallback:(NSArray<AliRtcUserVolumeInfo *> *)array totalVolume:(int)totalVolume {
    if ([self.delegate respondsToSelector:@selector(engine:onAudioVolumeCallback:totalVolume:)]) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.delegate engine:self.engine onAudioVolumeCallback:array totalVolume:totalVolume];
        });
    }
}

- (void)onUserAudioMuted:(NSString *)uid audioMuted:(BOOL)isMute {
    if ([self.delegate respondsToSelector:@selector(engine:onUserAudioMuted:audioMuted:)]) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.delegate engine:self.engine onUserAudioMuted:uid audioMuted:isMute];
        });
    }
}

- (void)onUserVideoMuted:(NSString *)uid videoMuted:(BOOL)isMute {
    if ([self.delegate respondsToSelector:@selector(engine:onUserVideoMuted:videoMuted:)]) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [self.delegate engine:self.engine onUserVideoMuted:uid videoMuted:isMute];
        });
    }
}
- (void)onNetworkQualityChanged:(NSString *)uid upNetworkQuality:(AliRtcNetworkQuality)upQuality downNetworkQuality:(AliRtcNetworkQuality)downQuality {
    if ([self.delegate respondsToSelector:@selector(engine:onNetworkQualityChanged:upNetworkQuality:downNetworkQuality:)]) {
        [self.delegate engine:self.engine onNetworkQualityChanged:uid upNetworkQuality:upQuality downNetworkQuality:downQuality];
    }
}

@end


static dispatch_once_t onceToken;
static RTCSmallClass *manager = nil;

@interface RTCSmallClass()<RTCEngineBridgeDelegate>

/// RTC Master Engine 
@property (strong, nonatomic) AliRtcEngine *masterEngine;


//@{@"channel":engine}
@property (strong, nonatomic) NSMutableDictionary *channelEngineDict;

//@{@"channel":userManager}
@property (strong,nonatomic) NSMutableDictionary *channelUserManagerDict;

/// 网络请求类
@property (strong, nonatomic) id<SmallClassFetcherProtocal> fetcher;


@end

@implementation RTCSmallClass

#pragma mark - 单例声明周期相关
+ (RTCSmallClass *) sharedInstance{
    dispatch_once(&onceToken, ^{
        manager = [[super allocWithZone:NULL] init];
    });
    return manager;
}

+ (instancetype)allocWithZone:(struct _NSZone *)zone {
    return [RTCSmallClass sharedInstance];
}

- (id)copyWithZone:(nullable NSZone *)zone {
    return [RTCSmallClass sharedInstance];
}

- (id)mutableCopyWithZone:(nullable NSZone *)zone {
    return [RTCSmallClass sharedInstance];
}

- (instancetype)init {
    self = [super init];
    if (self) {
        
    }
    return self;
}

-(void)dealloc
{
    NSLog(@"RTCSmallClass dealloc");
}


/// 销毁单例
-(void)destroySharedInstance
{
    [_channelUserManagerDict removeAllObjects];
    
    if(_masterEngine){
        //销毁子频道
        [_channelEngineDict enumerateKeysAndObjectsUsingBlock:^(id  _Nonnull key, id  _Nonnull obj, BOOL * _Nonnull stop) {
            if(obj !=_masterEngine) {
                [obj destroyChannel];
            }
        }];
        [_channelEngineDict removeAllObjects];
        
        [_masterEngine destroyChannel];
        [AliRtcEngine destroy];
        _masterEngine = nil;
    }
    onceToken = 0;
    manager = nil;
}

#pragma mark - private functions

/// 初始化sdk
- (void)initializeSDK
{
    //高音质模式
    [AliRtcEngine setAudioProfile:AliRtcEngineHighQualityMode
                      audio_scene:AliRtcSeneMediaMode];
    
    // 创建SDK实例，注册delegate，extras可以为空
    RTCEngineDelegate *delegate = [[RTCEngineDelegate alloc] initWithDelegate:self];
    _masterEngine = [AliRtcEngine sharedInstance:delegate
                                          extras:nil];
    delegate.engine = _masterEngine;
    //开启H5兼容模式
    [AliRtcEngine setH5CompatibleMode:YES];
    //使用扬声器
    [_masterEngine enableSpeakerphone:YES];
    //频道模式
    [_masterEngine setChannelProfile:AliRtcCommunication];
    
//    //推流分辨率
    [_masterEngine setVideoProfile:AliRtcVideoProfile_540_960P_15_1200Kb
                          forTrack:AliRtcVideoTrackCamera];
    
    //摄像头
    AliRtcCameraCapturerConfiguration *config = [[AliRtcCameraCapturerConfiguration alloc] init];
    config.preference = AliRtcCaptureOutputPreferencePreview;
    config.cameraDirection = AliRtcCameraDirectionFront;
    [_masterEngine setCameraCapturerConfiguration:config];
    
    //手动推流 手动拉流
    [_masterEngine setAutoPublish:NO withAutoSubscribe:NO];
    //摄像头角度
    [_masterEngine setDeviceOrientationMode:AliRtcOrientationModeAuto];
    
}


/// 创建子频道实例
- (AliRtcEngine *)createSubEngine{
    RTCEngineDelegate *delegate = [[RTCEngineDelegate alloc] initWithDelegate:self];
    AliRtcEngine *subEngine = [self.masterEngine createChannelWithDelegate:delegate extras:nil];
    delegate.engine = subEngine;
    [subEngine setAutoPublish:NO withAutoSubscribe:NO];
    
    return subEngine;
}


/// 登录
/// @param channel 频道
/// @param userName 用户昵称
- (void)login:(NSString *)channel userName:(NSString *)userName {
    AliRtcEngine *engine;
    NSInteger count =  self.channelEngineDict.count;
    if(count == 0) {
        //没有加入房间
        engine = self.masterEngine;
    } else {
        engine = [self createSubEngine];
    }
    [self.channelEngineDict setValue:engine forKey:channel];
//    __weak typeof(self) weakSelf = self;
//    [self.fetcher authInfo:@{@"channelId":channel} complete:^(AliRtcAuthInfo *info,NSString * errorMsg) {
//        if (!errorMsg) {
            AliRtcAuthInfo *info = [RTCSmallClass GenerateAliRtcAuthInfoWithChnnelId:channel];
            [engine joinChannel:info name:userName onResult:^(NSInteger errCode){
                NSLog(@"%ld",(long)errCode);
            }];
//        } else {
//            //请求鉴权错误
//            [weakSelf.channelEngineDict removeObjectForKey:channel];
//            if ([weakSelf.delegate respondsToSelector:@selector(channel:onJoinChannelResult:)] ) {
//                [weakSelf.delegate channel:channel onJoinChannelResult:-1];
//            }
//        }
//    }];
    
}

+ (AliRtcAuthInfo *)GenerateAliRtcAuthInfoWithChnnelId:(NSString *)channelID {
    NSString *AppID = @"" ;  //修改为自己的appid 该方案仅为开发测试使用，正式上线需要使用服务端的AppServer
    NSString *AppKey = @"";  //修改为自己的appkey 该方案仅为开发测试使用，正式上线需要使用服务端的AppServer
    
    NSString *userID = [NSString stringWithFormat:@"%d%d",(int)[[NSDate new] timeIntervalSince1970],(int)arc4random()];
   
    NSString *nonce = [NSString stringWithFormat:@"AK-%@",[[NSUUID UUID] UUIDString]];
   
    NSTimeInterval interval = 48 * 60 * 60;//48小时时间戳
    NSDate *datenow = [[NSDate date] initWithTimeIntervalSinceNow:interval];//现在时间
    long long timestamp = (long)(long)([datenow timeIntervalSince1970]*1000);
   
    //获取到token
    NSString *token = [NSString stringWithFormat:@"%@%@%@%@%@%lld",AppID,AppKey,channelID,userID,nonce,timestamp];
   
    //将token加密
    const char *s = [token cStringUsingEncoding:NSUTF8StringEncoding];
    NSData *keyData = [NSData dataWithBytes:s length:strlen(s)];
    uint8_t digest[CC_SHA256_DIGEST_LENGTH] = {0};
    CC_SHA256(keyData.bytes, (CC_LONG)keyData.length, digest);
    NSData *out = [NSData dataWithBytes:digest length:CC_SHA256_DIGEST_LENGTH];
    const unsigned *hashBytes = [out bytes];
    NSString *hash = [NSString stringWithFormat:@"%08x%08x%08x%08x%08x%08x%08x%08x",
    ntohl(hashBytes[0]), ntohl(hashBytes[1]), ntohl(hashBytes[2]),
    ntohl(hashBytes[3]), ntohl(hashBytes[4]), ntohl(hashBytes[5]),
    ntohl(hashBytes[6]), ntohl(hashBytes[7])];
    
//    NSString *nonce = [NSString stringWithFormat:@"AK-%@",[[NSUUID UUID] UUIDString]];
//    NSTimeInterval interval = 48 * 60 * 60;//48小时时间戳
//    NSDate *datenow = [[NSDate date] initWithTimeIntervalSinceNow:interval];//现在时间
//    long long timestamp = (long)(long)([datenow timeIntervalSince1970]*1000);
    NSArray *GSLB = @[@"https://rgslb.rtc.aliyuncs.com"];

    NSArray *agent = [NSArray array];
    AliRtcAuthInfo *authInfo = [[AliRtcAuthInfo alloc] init];
    authInfo.appid = AppID;
    authInfo.user_id = userID;
    authInfo.channel = channelID;
    authInfo.nonce = nonce;
    authInfo.timestamp = timestamp;
    authInfo.token = hash;
    authInfo.gslb = GSLB;
    authInfo.agent = agent;
    return authInfo;
}

/// 登出所有频道
- (void)logout {
    [self stopPreview];
    [self.channelEngineDict enumerateKeysAndObjectsUsingBlock:^(id  _Nonnull key, id  _Nonnull obj, BOOL * _Nonnull stop) {
        AliRtcEngine *engine = obj;
        [engine leaveChannel];
    }];
}


/// 开启本地预览
/// @param preview 预览的view
- (void)startPreview:(UIView *)preview {
    //创建renderView
    AliRenderView *renderView = nil;
    for (UIView *view in preview.subviews) {
        if ([view isKindOfClass:[AliRenderView class]]) {
            renderView = (AliRenderView *)view;
            break;
        }
    }
    
    if (!renderView) {
        renderView = [[AliRenderView alloc] init];
        [preview addSubview:renderView];
        [preview sendSubviewToBack:renderView];
    }
    
    renderView.frame = preview.bounds;
    
    AliVideoCanvas *canvas = [[AliVideoCanvas alloc] init];
    canvas.renderMode = AliRtcRenderModeAuto;
    canvas.view = renderView;
    [self.masterEngine setLocalViewConfig:canvas
                                 forTrack:AliRtcVideoTrackCamera];
    
    [self.masterEngine startPreview];
    
}

/// 停止本地预览
- (void)stopPreview {
    if (_masterEngine)
    {
        [_masterEngine setLocalViewConfig:nil
                                 forTrack:AliRtcVideoTrackCamera];
        [_masterEngine stopPreview];
    }
}


/// 选择摄像头
- (int)switchCamera {
    return [self.masterEngine switchCamera];
}

/// 禁用摄像头
- (int)muteLocalCamera:(BOOL)mute channel:(NSString *)channel {
    AliRtcEngine *engine = self.channelEngineDict[channel];
    if (engine) {
        return [engine muteLocalCamera:mute forTrack:AliRtcVideoTrackCamera];
    }
    return 0;
}
/// 静音
- (int)muteLocalMic:(BOOL)mute  channel:(NSString *)channel{
    AliRtcEngine *engine = self.channelEngineDict[channel];
    if (engine) {
        return [engine muteLocalMic:mute];
    }
    return 0;
}


/// 开始推流
/// @param channel 频道号
- (void)startPublish:(NSString *)channel {
    AliRtcEngine *engine = self.channelEngineDict[channel];
    if (engine) {
        NSLog(@"推流的频道:%@",channel);
        [engine configLocalCameraPublish:YES];
        [engine configLocalAudioPublish:YES];
        [engine publish:^(int errCode) {
            
        }];
    }
}

/// 停止推流
/// @param channel 频道号
- (void)stopPublish:(NSString *)channel {
    AliRtcEngine *engine = self.channelEngineDict[channel];
    if (engine) {
        //老师房间不推流
        [engine configLocalCameraPublish:NO];
        [engine configLocalAudioPublish:NO];
        [engine publish:^(int errCode) {
            
        }];
    }
}

#pragma mark - AliRtcEngineDelegate

- (void)engine:(AliRtcEngine *)engine onBye:(int)code {
    if(code == 1) {
    } else if (code == 2) {
        if ([self.delegate respondsToSelector:@selector(onRoomdestroy)])
        {
            [self.delegate onRoomdestroy];
        }
    }
}

- (void)engine:(AliRtcEngine *)engine onOccurError:(int)error
{
    NSString *channel = [self channalFromEngine:engine];
    
    if (error == AliRtcErrIceConnectionReconnectFail ||error == AliRtcErrSdkInvalidState || error == AliRtcErrIceConnectionHeartbeatTimeout || error == AliRtcErrSessionRemoved ) {
        if ([self.delegate respondsToSelector:@selector(channel:onSDKError:)])
        {
            [self.delegate channel:channel onSDKError:error];
        }
    } else {
        if ([self.delegate respondsToSelector:@selector(channel:onOccurError:)])
        {
            [self.delegate channel:channel onOccurError:error];
        }
    }
    
}

- (void)engine:(AliRtcEngine *)engine onOccurWarning:(int)warn
{
    NSString *channel = [self channalFromEngine:engine];
    if ([self.delegate respondsToSelector:@selector(channel:onOccurWarning:)])
    {
        [self.delegate channel:channel onOccurWarning:warn];
    }
}
//流变化的回调
- (void)engine:(AliRtcEngine *)engine onRemoteTrackAvailableNotify:(NSString *)uid audioTrack:(AliRtcAudioTrack)audioTrack videoTrack:(AliRtcVideoTrack)videoTrack {
    //用户停止推流
    if(videoTrack == AliRtcVideoTrackNo && audioTrack == AliRtcAudioTrackNo) {
        // 更新用户信息
        NSString *channel = [self channalFromEngine:engine];
        SmallClassRemoteUserManager *userManager = [self userManager:channel];
        [userManager updateRemoteUser:uid forTrack:AliRtcVideoTrackNo];
        
        if([self.delegate respondsToSelector:@selector(channel:onRemoteTrace:avaliable:)]) {
            [self.delegate  channel:channel onRemoteTrace:uid avaliable:NO];
        }
        //停止推流
        if ([self.delegate respondsToSelector:@selector(channel:onUserSpeaking:speaking:displayName:)]) {
            NSString *displayName = [self displayName:uid inEngine:engine];
            [self.delegate channel:channel onUserSpeaking:uid speaking:NO displayName:displayName];
        }
    }
    
    //订阅远端流
    if(audioTrack !=AliRtcAudioTrackNo && videoTrack != AliRtcVideoTrackNo ) {
        //判断是不是老师 老师订阅大流 学生订阅小流
        NSString *displayName = [self displayName:uid inEngine:engine];
        BOOL preferMaster = [displayName containsString:@"老师"];
        
        if (videoTrack == AliRtcVideoTrackBoth) {
            [engine  configRemoteScreenTrack:uid enable:YES];
            preferMaster = NO;
        }
        [engine configRemoteCameraTrack:uid preferMaster:preferMaster enable:YES];
        [engine configRemoteAudio:uid enable:YES];
        [engine subscribe:uid onResult:^(NSString * _Nonnull uid, AliRtcVideoTrack vt, AliRtcAudioTrack at) {
            
        }];
    }
    
    //如果停止推送音频流 回调说话接口
    if (audioTrack == AliRtcAudioTrackNo) {
        NSString *channel = [self channalFromEngine:engine];
        SmallClassRemoteUserManager *userManager = [self userManager:channel];
        
        NSArray *users = [userManager findUser:uid];
        
        for (SmallClassRemoteUser *user in users) {
            if(user.isSpeaking != NO) {
                user.isSpeaking = NO;
                if ([self.delegate respondsToSelector:@selector(channel:onUserSpeaking:speaking:displayName:)]) {
                    [self.delegate channel:channel onUserSpeaking:uid speaking:NO displayName:user.displayName];
                }
            }
        }
    }
}

//订阅变化的回调
- (void)engine:(AliRtcEngine *)engine onSubscribeChangedNotify:(NSString *)uid audioTrack:(AliRtcAudioTrack)audioTrack videoTrack:(AliRtcVideoTrack)videoTrack {
    
    //把userMode 添加到 userManager中
    NSString *displayName = [self displayName:uid inEngine:engine];
    
    NSString *channel = [self channalFromEngine:engine];

    SmallClassRemoteUserManager *userManager = [self userManager:channel];
    [userManager  updateRemoteUser:uid forTrack:videoTrack];
    [userManager updateRemoteUer:uid forDisplayName:displayName];
    
    if (videoTrack == AliRtcVideoTrackCamera) {
        //设置canvas
        AliVideoCanvas *canvas = [[AliVideoCanvas alloc] init];
        canvas.renderMode = AliRtcRenderModeFill;
        canvas.view = [userManager cameraView:uid];
        [engine setRemoteViewConfig:canvas uid:uid forTrack:AliRtcVideoTrackCamera];
    }else if(videoTrack == AliRtcVideoTrackScreen)   {
        //设置canvas
        AliVideoCanvas *canvas = [[AliVideoCanvas alloc] init];
        canvas.renderMode = AliRtcRenderModeFill;
        canvas.view = [userManager screenView:uid];
        [engine setRemoteViewConfig:canvas uid:uid forTrack:AliRtcVideoTrackScreen];
    } else if (videoTrack == AliRtcVideoTrackBoth) {
        //都设置
        AliVideoCanvas *cameraCanvas = [[AliVideoCanvas alloc] init];
        cameraCanvas.renderMode = AliRtcRenderModeFill;
        cameraCanvas.view = [userManager cameraView:uid];
        [engine setRemoteViewConfig:cameraCanvas uid:uid forTrack:AliRtcVideoTrackCamera];
        
        AliVideoCanvas *screenCanvas = [[AliVideoCanvas alloc] init];
        screenCanvas.renderMode = AliRtcRenderModeFill;
        screenCanvas.view = [userManager screenView:uid];
        [engine setRemoteViewConfig:screenCanvas uid:uid forTrack:AliRtcVideoTrackScreen];
    }

    //订阅成功
    if([self.delegate respondsToSelector:@selector(channel:onSubscribe:result:)]) {
        [self.delegate  channel:channel onSubscribe:uid result:YES];
    }
}

//用户是否静音的回调
- (void)engine:(AliRtcEngine *)engine onUserAudioMuted:(NSString *)uid audioMuted:(BOOL)isMute {
    NSString *channel = [self channalFromEngine:engine];
    SmallClassRemoteUserManager *userManager = [self userManager:channel];
    [userManager updateRemoteUer:uid forAudioMuted:isMute];
    
    if ([self.delegate respondsToSelector:@selector(channel:onUserAudioMuted:audioMuted:)]) {
        [self.delegate channel:channel onUserAudioMuted:uid audioMuted:isMute];
    }
}

//用户是否禁用摄像头的回调
- (void)engine:(AliRtcEngine *)engine onUserVideoMuted:(NSString *)uid videoMuted:(BOOL)isMute {
    NSString *channel = [self channalFromEngine:engine];
    SmallClassRemoteUserManager *userManager = [self userManager:channel];
    [userManager updateRemoteUer:uid forVideoMuted:isMute];
    
    if ([self.delegate respondsToSelector:@selector(channel:onUserVideoMuted:videoMuted:)]) {
        [self.delegate channel:channel onUserVideoMuted:uid videoMuted:isMute];
    }
}

//用户音量变化回调
- (void)engine:(AliRtcEngine *)engine onAudioVolumeCallback:(NSArray <AliRtcUserVolumeInfo *> *)array totalVolume:(int)totalVolume {
    
    NSString *channel = [self channalFromEngine:engine];
    SmallClassRemoteUserManager *userManager = [self userManager:channel];
    
    for (AliRtcUserVolumeInfo *volumeInfo in array)
    {
        //uid 为1 代表的是总音量
        if([volumeInfo.uid isEqualToString:@"1"])
        {
            continue;
        }
        //如果uid是0   或者不存在表示的是自己
        if([volumeInfo.uid isEqualToString:@"0"]||volumeInfo.uid.length == 0)
        {
            continue;
        }
        NSArray *users = [userManager findUser:volumeInfo.uid];
        for (SmallClassRemoteUser *user in users) {
            if(user.isSpeaking != volumeInfo.speech_state) {
                user.isSpeaking = volumeInfo.speech_state;
                if ([self.delegate respondsToSelector:@selector(channel:onUserSpeaking:speaking:displayName:)]) {
                    [self.delegate channel:channel onUserSpeaking:volumeInfo.uid speaking:volumeInfo.speech_state displayName:user.displayName];
                } 
            }
        }
    }
}

// 自己离开频道回调
- (void)engine:(AliRtcEngine *)engine onLeaveChannelResult:(int)result
{
    if ([self.delegate respondsToSelector:@selector(channel:onLeaveChannelResult:)]) {
        NSString *channel = [self channalFromEngine:engine];
        [self.delegate channel:channel onLeaveChannelResult:result];
    }
}
// 自己加入频道回调
-(void)engine:(AliRtcEngine *)engine onJoinChannelResult:(int)result authInfo:(AliRtcAuthInfo *)authInfo {
    NSString *channel = [self channalFromEngine:engine];
    
    if (result != 0) {
        [self.channelEngineDict removeObjectForKey:channel];
    }
    [engine enableAudioVolumeIndication:500 smooth:3 reportVad:1];
    if ([self.delegate respondsToSelector:@selector(channel:onJoinChannelResult:)]) {
        NSString *channel = [self channalFromEngine:engine];
        [self.delegate channel:channel onJoinChannelResult:result];
    }
    //子频道推流 主频道不推流
    if (result == 0 && engine != _masterEngine) {
          NSLog(@"推流的频道:%@",channel);
          [engine configLocalCameraPublish:YES];
          [engine configLocalAudioPublish:YES];
          [engine publish:^(int errCode) {}];
      }
    
    
}

// 远端用户上线通知
- (void)engine:(AliRtcEngine *)engine onRemoteUserOnLineNotify:(NSString *)uid {
    
    NSString *channel = [self channalFromEngine:engine];
    SmallClassRemoteUserManager *userManager = [self userManager:channel];
    //把userMode 添加到 userManager中
    NSString *displayName = [self displayName:uid inEngine:engine];
    [userManager  updateRemoteUser:uid forTrack:AliRtcVideoTrackNo];
    [userManager updateRemoteUer:uid forDisplayName:displayName];
    
    if([self.delegate respondsToSelector:@selector(channel:onJoin:)]) {
        [self.delegate channel:channel onJoin:uid];
    }
}
// 远端用户下线通知
- (void)engine:(AliRtcEngine *)engine onRemoteUserOffLineNotify:(NSString *)uid {
    NSString *channel = [self channalFromEngine:engine];
    SmallClassRemoteUserManager *userManager = [self userManager:channel];
    [userManager remoteUserOffLine:uid];
    
    if([self.delegate respondsToSelector:@selector(channel:onLeave:)]) {
        [self.delegate channel:channel onLeave:uid];
        if ([self.delegate respondsToSelector:@selector(channel:onUserSpeaking:speaking:displayName:)]) {
            NSString *displayName = [self displayName:uid inEngine:engine];
            [self.delegate channel:channel onUserSpeaking:uid speaking:NO displayName:displayName];
        }
    }
}

// 网络变化通知
- (void)engine:(AliRtcEngine *)engine onNetworkQualityChanged:(NSString *)uid
upNetworkQuality:(AliRtcNetworkQuality)upQuality
downNetworkQuality:(AliRtcNetworkQuality)downQuality
{
    if ([self.delegate respondsToSelector:@selector(onNetworkQualityChanged:upNetworkQuality:downNetworkQuality:)])
    {
        [self.delegate onNetworkQualityChanged:uid
                              upNetworkQuality:upQuality
                            downNetworkQuality:downQuality];
    }
}

#pragma mark setter & getter

- (AliRtcEngine *)masterEngine {
    if (!_masterEngine) {
        [self initializeSDK];
    }
    return _masterEngine;
}

- (id<SmallClassFetcherProtocal>)fetcher
{
#warning 修改 SmallClassSeatsLoaderFactory 工厂类 创建自己的麦序类
    if (!_fetcher) {
        _fetcher = [SmallClassFetcherFactory getFetcher:KSmallClassFetherDefault];
    }
    NSAssert(_fetcher != nil, @"请初始化获取授权信息的对象");
    return _fetcher;
}

- (NSMutableDictionary *)channelEngineDict {
    if (!_channelEngineDict) {
        _channelEngineDict = [@{} mutableCopy];
    }
    return _channelEngineDict;
}

- (NSString *)channalFromEngine:(AliRtcEngine *)engine {
    __block NSString *channel = @"";
    [self.channelEngineDict enumerateKeysAndObjectsUsingBlock:^(id  _Nonnull key, id  _Nonnull obj, BOOL * _Nonnull stop) {
        if (engine == obj) {
            channel = key;
        }
    }];
    return channel;
}


- (SmallClassRemoteUserManager *)userManager:(NSString *)channel {
    
    SmallClassRemoteUserManager *userManager = self.channelUserManagerDict[channel];
    if (!userManager) {
        userManager = [[SmallClassRemoteUserManager alloc] init];
        [self.channelUserManagerDict setValue:userManager forKey:channel];
    }
    return userManager;
}

- (NSMutableDictionary *)channelUserManagerDict {
    if (!_channelUserManagerDict) {
        _channelUserManagerDict = @{}.mutableCopy;
    }
    return _channelUserManagerDict;
}

- (NSArray *)getRemoteUserList:(NSString *)channel {
    SmallClassRemoteUserManager *userManager = [self userManager:channel];
    return [userManager allOnlineUsers];
}

- (NSString *)displayName:(NSString *)uid inEngine:(AliRtcEngine *)engine {
    NSString *displayName = @"";
    NSString *displayName_utf8 = [engine getUserInfo:uid][@"displayName"];
    
    if (displayName_utf8) {
        displayName = [NSString stringWithUTF8String:[displayName_utf8 cString]];
    }
    return displayName;
}
// 获取用户信息
- (NSDictionary *)getUserInfo:(NSString *)uid channel:(NSString *)channel {
    AliRtcEngine *engine = self.channelEngineDict[channel];
    if (engine) {
        return [engine getUserInfo:uid];
    }
    return nil;
}
@end
