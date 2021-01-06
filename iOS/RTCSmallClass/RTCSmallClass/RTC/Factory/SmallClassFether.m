//
//  SmallClassAuthorization.m
//  RTCAudioLiveRoom
//
//  Created by Aliyun on 2020/7/28.
//

#import "SmallClassFether.h"
#import "AppConfig.h"
#import "NetworkManager.h"
#import "MJExtension.h"

@implementation SmallClassFether

- (void)authInfo:(NSDictionary *)params
        complete:(void(^)(AliRtcAuthInfo *info,NSString *errorMsg)) handler
{
    NSString *url = [kBaseUrl_SmallClass stringByAppendingString:@"getRtcAuth"];
    
    [NetworkManager GET:url
             parameters:params
      completionHandler:^(NSString * _Nullable errString,NSDictionary * _Nullable resultDic)
     {
        if(!errString) {
            AliRtcAuthInfo *authInfo = [[AliRtcAuthInfo alloc] init];
            authInfo.appid = resultDic[@"appid"];
            authInfo.user_id = resultDic[@"userid"];
            authInfo.channel = resultDic[@"channelId"];
            authInfo.nonce = resultDic[@"nonce"];
            authInfo.timestamp = [resultDic[@"timestamp"] longLongValue];
            authInfo.token = resultDic[@"token"];
            authInfo.gslb = resultDic[@"gslb"];
            authInfo.agent = resultDic[@"agent"];
            handler(authInfo,nil);
        }else{
            handler(nil,errString);
        }
    }];
}


- (void)getplayUrl:(NSDictionary *)params
          complete:(void(^)(NSDictionary *result,NSString *errorMsg)) handler
{
    NSString *url = [kBaseUrl_SmallClass stringByAppendingString:@"getPlayUrl"];
    [NetworkManager GET:url
             parameters:params
      completionHandler:^(NSString * _Nullable errString,NSDictionary * _Nullable resultDic)
     {
        if(!errString) {
            handler(resultDic[@"playUrl"],errString);
        }else{
            handler(nil,errString);
        }
    }];
}


- (void)kickout:(NSDictionary *)params
       complete:(void(^)(NSString *error))handler
{
    NSString *url = [kBaseUrl_SmallClass stringByAppendingString:@"removeTerminals"];
    [NetworkManager POST:url
              parameters:params
       completionHandler:^(NSString * _Nullable errString,NSDictionary * _Nullable resultDic)
     {
        handler(errString);
    }];
}


- (void)getUserList:(NSDictionary *)params
           complete:(void(^)(NSArray *userList,NSString *error))handler
{
    NSString *url = [kBaseUrl_SmallClass stringByAppendingString:@"getUserList"];
    
    [NetworkManager GET:url
             parameters:params
      completionHandler:^(NSString * _Nullable errString, id  _Nullable result)
     {
        if (errString)
        {
            handler(nil,errString);
        } else {
            if ([result isKindOfClass:[NSArray class]])
            {
                NSLog(@"我的日志：getSeatList");
                NSArray *array = [NSString mj_objectArrayWithKeyValuesArray:result];
                NSLog(@"%@",array);
                handler(array,nil);
            } else {
                handler(nil,@"没有获取到人数");
            }
        }
    }];
}


@end
