//
//  RTCAudioliveRoomManager.h
//  LectureHall
//
//  Created by Aliyun on 2020/6/15.
//  Copyright © 2020 alibaba. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <AliRTCSdk/AliRTCSdk.h>
#import "SmallClassRemoteUserManager.h"

NS_ASSUME_NONNULL_BEGIN

@protocol RTCSmallClassDelegate <NSObject>

@optional

/// 网络变化判断
/// @param uid 用户id
/// @param upQuality 上行网络质量
/// @param downQuality 下行网络质量
- (void)onNetworkQualityChanged:(NSString *)uid
               upNetworkQuality:(AliRtcNetworkQuality)upQuality
             downNetworkQuality:(AliRtcNetworkQuality)downQuality;

/// 用户说话状态改变
/// @param channel 频道
/// @param uid 用户id
/// @param speaking 是否正在说话
/// @param name 用户昵称
- (void)channel:(NSString *)channel onUserSpeaking:(NSString *)uid speaking:(BOOL) speaking displayName:(NSString *)name;

/// 用户是否静音
/// @param channel 频道
/// @param uid 用户id
/// @param isMute 用户是否静音
- (void)channel:(NSString *)channel onUserAudioMuted:(NSString *)uid audioMuted:(BOOL)isMute;

/// 用户是否禁用摄像头
/// @param channel 频道
/// @param uid 用户id
/// @param isMute 是否禁用
- (void)channel:(NSString *)channel onUserVideoMuted:(NSString *)uid videoMuted:(BOOL)isMute;

/// 离开频道
/// @param channel 频道
/// @param result 结果
- (void)channel:(NSString *)channel onLeaveChannelResult:(int)result ;

/// 加入频道
/// @param channel 频道
/// @param result 结果
- (void)channel:(NSString *)channel onJoinChannelResult:(int)result;

/// 警告
/// @param channel 频道
/// @param warn 警告码
- (void)channel:(NSString *)channel onOccurWarning:(int)warn;

/// 错误码
/// @param channel 频道
/// @param error 错误码
- (void)channel:(NSString *)channel onOccurError:(int)error;


/// sdk严重错误 需要销毁引擎
/// @param channel 频道
/// @param error 错误码
- (void)channel:(NSString *)channel onSDKError:(int)error;

/// 房间被销毁通知
- (void)onRoomdestroy;

/// 远端用户加入频道
/// @param channel 频道
/// @param uid 用户id
- (void)channel:(NSString *)channel onJoin:(NSString *)uid;

/// 远端用户离开频道
/// @param channel 频道
/// @param uid 用户id
- (void)channel:(NSString *)channel onLeave:(NSString *)uid;

/// 订阅成功
/// @param channel 频道
/// @param uid 用户id
/// @param result 结果
- (void)channel:(NSString *)channel onSubscribe:(NSString *)uid result:(BOOL)result;

/// 远端流变化
/// @param channel 频道
/// @param uid 用户id
/// @param abaliable 是否可用
- (void)channel:(NSString *)channel onRemoteTrace:(NSString *)uid avaliable:(BOOL)abaliable;

@end

@interface RTCSmallClass : NSObject

@property (nonatomic, weak) id<RTCSmallClassDelegate> delegate;


//MARK: - 单例创建与销毁
//////////////////////////////////////////////////////////////
///
///        单例创建与销毁
///
//////////////////////////////////////////////////////////////
///
/// @brief 获取单例
/// @return RTCAudioliveRoomManager 单例对象
+ (RTCSmallClass *) sharedInstance;

/// 销毁RTCSDK
- (void)destroySharedInstance;


/// 加入频道
/// @param mainChannel   主频道
/// @param userName   任意用于显示的用户名称。不是User ID
/// @param handler   回调
//- (void)login:(NSString *)mainChannel subChannels:(NSArray <NSString *>*)subChannels
//     userName:(NSString *)userName       complete:(void(^)(NSInteger errorCode))handler;


/// 加入频道
/// @param channel 频道
/// @param userName 用户昵称
- (void)login:(NSString *)channel userName:(NSString *)userName;

/// 离开所有频道
- (void)logout;

/// 开始本地预览
/// @param preview 预览的view
- (void)startPreview:(UIView *)preview;

/// 停止本地预览
- (void)stopPreview;

/// 旋转摄像头
- (int)switchCamera;

/// 静音
/// @param mute 是否静音
/// @param channel 频道
- (int)muteLocalMic:(BOOL)mute channel:(NSString *)channel;

/// 禁用摄像头
/// @param mute 是否禁用
/// @param channel 频道
- (int)muteLocalCamera:(BOOL)mute channel:(NSString *)channel;

/// 获取某个频道远端用户列表
/// @param channel 频道
- (NSArray *)getRemoteUserList:(NSString *)channel;

/// 开始推流
/// @param channel 频道
- (void)startPublish:(NSString *)channel;

/// 停止推流
/// @param channel 频道
- (void)stopPublish:(NSString *)channel;

/// 获取用户信息
/// @param uid 用户id
/// @param channel 频道
- (NSDictionary *)getUserInfo:(NSString *)uid channel:(NSString *)channel;



@end

NS_ASSUME_NONNULL_END
