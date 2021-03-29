//
//  ResultViewController.m
//  MidObjcSample
//
//  Created by Muhammad Fauzi Masykur on 25/03/21.
//  Copyright © 2021 Muhammad Fauzi Masykur. All rights reserved.
//

#import "ResultViewController.h"

@interface ResultViewController ()

@end

@implementation ResultViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self.paymentStatusLabel setText:self.paymentStatus];
}
@end
