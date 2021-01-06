//
//  AppDelegate.m
//  RTCSolution
//
//  Created by Aliyun on 2020/6/28.
//  Copyright © 2020 Aliyun. All rights reserved.
//

#import "AppDelegate.h"
#import "RTCHomeViewController.h"
#import "RTCNavigationController.h"

#define plistUrl @"https://alivc-demo-cms.alicdn.com/versionProduct/installPackage/RTC_Solution/RTCSolution.plist"

@interface AppDelegate () <UIAlertViewDelegate>

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    // Override point for customization after application launch.
    self.window =[[UIWindow alloc]initWithFrame:[UIScreen mainScreen].bounds];
    RTCNavigationController *nav =[[RTCNavigationController alloc]initWithRootViewController:[RTCHomeViewController new]];
    [self.window setRootViewController:nav];
    [self.window makeKeyAndVisible];
 
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application {
    NSLog(@"\n ===> 程序暂停 !");
}


- (void)applicationDidEnterBackground:(UIApplication *)application {
    NSLog(@"\n ===> 进入后台 ！");
}


#pragma mark - 版本更新

- (void)checkVersion{
    
//    NSString *plistString = @"https://alivc-demo-cms.alicdn.com/versionProduct/installPackage/RTC_Solution/RTCSolution.plist";
    
    
    NSString *releaseNoteString = [self releaseNoteStringWithString:plistUrl];

    if (releaseNoteString) {
       
        dispatch_async(dispatch_get_main_queue(), ^{
            UIAlertView *alertView  = [[UIAlertView alloc] initWithTitle:@"更新提示" message:releaseNoteString delegate:self cancelButtonTitle:nil otherButtonTitles:@"立即更新", nil];
            alertView.delegate = self;
            UIAlertController *alertVC = [alertView valueForKey:@"_alertController"];
            NSArray *subViews = alertVC.view.subviews[0].subviews[0].subviews[0].subviews[0].subviews[0].subviews;
            UILabel *messageLb = subViews[2];//message
            messageLb.textAlignment = NSTextAlignmentLeft;
            [alertView show];
        });
      
        
    }
}
 
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
//      NSString *plistString = @"https://alivc-demo-cms.alicdn.com/versionProduct/installPackage/RTC_Solution/RTCSolution.plist";
    
    if (@available(iOS 10.0, *)) {
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"itms-services://?action=download-manifest&url=%@",plistUrl]] options:@{} completionHandler:nil];
    } else {
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:[NSString stringWithFormat:@"itms-services://?action=download-manifest&url=%@",plistUrl]]];
    }
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        exit(0);
    });
}
/**
 检查本地版本号与服务器版本号，看下有无更新
 
 @param plistString 服务器版本号所在的url字符串
 @return nil - 无更新， 有值 - 有更新并且返回更新内容
 */
- (NSString *)releaseNoteStringWithString:(NSString *)plistString{
    
    
    NSDictionary *dic = [NSDictionary dictionaryWithContentsOfURL:[NSURL URLWithString:plistString]];
    NSString *releaseNote = dic[@"items"][0][@"metadata"][@"releaseNote"];
    NSString *onLineVersion = dic[@"items"][0][@"metadata"][@"bundle-version"];
    
    releaseNote = [releaseNote stringByReplacingOccurrencesOfString:@"\\n" withString:@"\n"];
    
    NSString *localVerson = [[NSBundle mainBundle] objectForInfoDictionaryKey:@"CFBundleShortVersionString"];
    
    if([localVerson compare:onLineVersion options:NSNumericSearch] == NSOrderedAscending){
        return releaseNote;
    }
    return nil;
}

@end
