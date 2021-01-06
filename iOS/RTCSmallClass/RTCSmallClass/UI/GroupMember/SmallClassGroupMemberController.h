//
//  SmallClassGroupMemberController.h
//  RTCSmallClass
//
//  Created by 孙震 on 2020/9/28.
//

#import <UIKit/UIKit.h>
#import "SmallClassRemoteUser.h"

NS_ASSUME_NONNULL_BEGIN

@interface SmallClassGroupMemberController : UITableViewController

@property (nonatomic, strong) NSArray<SmallClassRemoteUser *> *userList;

@end

NS_ASSUME_NONNULL_END
