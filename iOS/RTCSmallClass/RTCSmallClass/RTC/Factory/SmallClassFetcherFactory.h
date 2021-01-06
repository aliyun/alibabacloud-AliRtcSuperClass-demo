//
//  VideoRoomSeatsLoaderFactory.h
//  Pods
//
//  Created by Aliyun on 2020/7/22.
//

#import <Foundation/Foundation.h>
#import "SmallClassFetcherProtocal.h"
 
#define KSmallClassFetherDefault @"SmallClassFetherDefault"

NS_ASSUME_NONNULL_BEGIN

@interface SmallClassFetcherFactory : NSObject
 

+ (id<SmallClassFetcherProtocal>)getFetcher:(NSString *)type;



@end

NS_ASSUME_NONNULL_END
