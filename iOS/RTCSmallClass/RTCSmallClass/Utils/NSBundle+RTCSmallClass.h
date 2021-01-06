//
//  NSBundle+RTCSmallClass.h
//  Pods
//
//  Created by aliyun on 2020/8/6.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSBundle (RTCSmallClass)

+ (instancetype)RTCSmallClassBundle;

+ (UIImage *)RSC_imageWithName:(NSString *)name type:(NSString *)type;

+ (UIImage *)RSC_pngImageWithName:(NSString *)name;

+ (UIStoryboard *)RSC_storyboard;

+ (NSString *)RSC_musicPathForResource:(NSString *)name;

@end

NS_ASSUME_NONNULL_END
