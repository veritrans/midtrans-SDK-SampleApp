//
//  ResultViewController.h
//  MidObjcSample
//
//  Created by Muhammad Fauzi Masykur on 25/03/21.
//  Copyright © 2021 Muhammad Fauzi Masykur. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface ResultViewController : UIViewController
@property (weak, nonatomic) IBOutlet UILabel *paymentStatusLabel;
@property (nonatomic) NSString *paymentStatus;

@end

NS_ASSUME_NONNULL_END
