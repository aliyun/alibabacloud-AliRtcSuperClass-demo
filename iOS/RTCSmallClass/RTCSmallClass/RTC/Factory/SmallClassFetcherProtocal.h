//
//  SmallClassFetcherProtocal.h
//  RTCAudioLiveRoom
//
//  Created by Aliyun on 2020/7/28.
//

#ifndef SmallClassFetcherProtocal_h
#define SmallClassFetcherProtocal_h
#import <AliRTCSdk/AliRTCSdk.h>
@protocol SmallClassFetcherProtocal <NSObject>

/// 通过channelId 生成AuthInfo
/// @param params 参数 例如 :@{@"channelId":channelId,@"userId":userId}
/// @param handler 回调处理
- (void)authInfo:(NSDictionary *)params
        complete:(void(^)(AliRtcAuthInfo *info,NSString *errorMsg)) handler;

/// 获取播放的链接
/// @param params 例如 @{@"channelId":@“11111”}
/// @param handler 回调处理
- (void)getplayUrl:(NSDictionary *)params
          complete:(void(^)(NSDictionary *result,NSString *errorMsg)) handler;

/// 踢出房间的其他用户（除了操作者）
/// @param params 例如 @{@"channelId":self.channelId,@"operatorId":@“操作者id”}
/// @param handler 回调处理
- (void)kickout:(NSDictionary *)params
       complete:(void(^)(NSString *error))handler;


/// 获取房间用户列表 按加入时间排序
/// @param params @{@"channelId":@“1111”}
/// @param handler 回调处理
- (void)getUserList:(NSDictionary *)params
           complete:(void(^)(NSArray *userList,NSString *error))handler;

@end

#endif /* SmallClassFetcherProtocal_h */
