//
//  AudioRoomLoginApi.h
//  RTCAudioLiveRoom
//
//  Created by Aliyun on 2020/7/7.
//

#import <Foundation/Foundation.h>
#import <AliRTCSdk/AliRTCSdk.h>


NS_ASSUME_NONNULL_BEGIN

@interface SmallClassApi : NSObject



/// 生成随机图片
+ (void)randomCoverUrl:(void(^)(NSString *coverUrl,NSString *error))handler;


/// 开启旁路直播
/// @param channelId 频道
/// @param userId 用户id
/// @param coverUrl 封面图片
/// @param title 标题
/// @param handler 回调处理
+ (void)startMPUTask:(NSString *)channelId
              userId:(NSString *)userId
            coverUrl:(NSString *)coverUrl
               title:(NSString *)title
            complete:(void(^)(NSString *error))handler;

/// 更新旁路布局
/// @param channelId 频道id
/// @param handler  回调处理
+ (void)updateMPULayout:(NSString *)channelId
            complete:(void(^)(NSString *error))handler;


/// 获取房间人数
/// @param channelId 频道id
/// @param handler 回调处理
+ (void)getUserList:(NSString *)channelId
           complete:(void(^)(NSArray *userList,NSString *error))handler;


/// 踢出房间的连线用户
/// @param channel 频道
/// @param handler 回调处理
+ (void)channelInfo:(NSString *)channel
           complete:(void(^)(NSDictionary *info,NSString *error))handler;

/// 停止旁路直播
/// @param channelId 频道
/// @param handler 回调处理
+ (void)stopMPUTask:(NSString *)channelId
           complete:(void(^)(NSString *error))handler;
@end

NS_ASSUME_NONNULL_END
