package com.sage.cabapp;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.sage.cabapp.ui.accountsettings.AccountSettingsViewModel;
import com.sage.cabapp.ui.addbusinessprofile.AddBusinessProfileViewModel;
import com.sage.cabapp.ui.addbusinessprofile.NewBusinessProfileViewModel;
import com.sage.cabapp.ui.addemailsignup.AddEmailSignupViewModel;
import com.sage.cabapp.ui.addhomeplace.AddHomePlaceViewModel;
import com.sage.cabapp.ui.addnamesignup.AddNameSignupViewModel;
import com.sage.cabapp.ui.addpaymentmethod.AddPaymentMethodViewModel;
import com.sage.cabapp.ui.addpromocode.AddPromoCodeViewModel;
import com.sage.cabapp.ui.addriverrating.AddDriverRatingViewModel;
import com.sage.cabapp.ui.addrivertip.AddDriverTipViewModel;
import com.sage.cabapp.ui.addworkplace.AddWorkPlaceViewModel;
import com.sage.cabapp.ui.chatmodulenew.ChatModuleNewViewModel;
import com.sage.cabapp.ui.confirmpickup.ConfirmPickupMapViewModel;
import com.sage.cabapp.ui.driverprofile.DriverProfileViewModel;
import com.sage.cabapp.ui.editbusinessprofile.EditBusinessProfileViewModel;
import com.sage.cabapp.ui.editcard.EditCardViewModel;
import com.sage.cabapp.ui.editemail.EditEmailViewModel;
import com.sage.cabapp.ui.editname.EditNameViewModel;
import com.sage.cabapp.ui.editphone.EditPhoneViewModel;
import com.sage.cabapp.ui.editphone.UpdatePhoneViewModel;
import com.sage.cabapp.ui.freerides.FreeRidesViewModel;
import com.sage.cabapp.ui.help.GeneralInquiryFormViewModel;
import com.sage.cabapp.ui.help.HelpViewModel;
import com.sage.cabapp.ui.howtopay.HowToPayViewModel;
import com.sage.cabapp.ui.locationonmap.LocationOnMapViewModel;
import com.sage.cabapp.ui.main.HomeViewModel;
import com.sage.cabapp.ui.payment.PaymentViewModel;
import com.sage.cabapp.ui.paymentprofile.ChangePaymentProfileViewModel;
import com.sage.cabapp.ui.personalprofile.PersonalProfileViewModel;
import com.sage.cabapp.ui.phoneverification.PhoneViewModel;
import com.sage.cabapp.ui.promocode.PromoCodeViewModel;
import com.sage.cabapp.ui.requestaccepted.RequestAcceptedViewModel;
import com.sage.cabapp.ui.requestforcar.RequestForCarViewModel;
import com.sage.cabapp.ui.savedplaces.SavedPlacesViewModel;
import com.sage.cabapp.ui.setaddress.SetAddressViewModel;
import com.sage.cabapp.ui.setaddressconfirmpickup.SetAddressPickupViewModel;
import com.sage.cabapp.ui.splash.SplashViewModel;
import com.sage.cabapp.ui.tipthemmore.TipThemMoreViewModel;
import com.sage.cabapp.ui.tripfare.TripFareViewModel;
import com.sage.cabapp.ui.triphistory.TripHistoryViewModel;
import com.sage.cabapp.ui.tripinquirypage.TripInquiryViewModel;
import com.sage.cabapp.ui.tripreceipt.TripReceiptViewModel;
import com.sage.cabapp.ui.tutorial.TutorialViewModel;
import com.sage.cabapp.ui.updateroute.UpdateRouteViewModel;
import com.sage.cabapp.ui.verifyotp.VerifyOTPViewModel;
import com.sage.cabapp.utils.rx.SchedulerProvider;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Maroof Ahmed Siddique on 14-11-2019.
 * Lala
 * maroofahmedsiddique@gmail.com
 */
@Singleton
public class ViewModelProviderFactory extends ViewModelProvider.NewInstanceFactory {

    private final SchedulerProvider schedulerProvider;

    @Inject
    public ViewModelProviderFactory(SchedulerProvider schedulerProvider) {
        this.schedulerProvider = schedulerProvider;
    }

    @NotNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SplashViewModel.class)) {
            //noinspection unchecked
            return (T) new SplashViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(PhoneViewModel.class)) {
            //noinspection unchecked
            return (T) new PhoneViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(TutorialViewModel.class)) {
            //noinspection unchecked
            return (T) new TutorialViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(VerifyOTPViewModel.class)) {
            //noinspection unchecked
            return (T) new VerifyOTPViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(AddEmailSignupViewModel.class)) {
            //noinspection unchecked
            return (T) new AddEmailSignupViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(AddNameSignupViewModel.class)) {
            //noinspection unchecked
            return (T) new AddNameSignupViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(HowToPayViewModel.class)) {
            //noinspection unchecked
            return (T) new HowToPayViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            //noinspection unchecked
            return (T) new HomeViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(SetAddressViewModel.class)) {
            //noinspection unchecked
            return (T) new SetAddressViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(LocationOnMapViewModel.class)) {
            //noinspection unchecked
            return (T) new LocationOnMapViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(RequestForCarViewModel.class)) {
            //noinspection unchecked
            return (T) new RequestForCarViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(ConfirmPickupMapViewModel.class)) {
            //noinspection unchecked
            return (T) new ConfirmPickupMapViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(SetAddressPickupViewModel.class)) {
            //noinspection unchecked
            return (T) new SetAddressPickupViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(RequestAcceptedViewModel.class)) {
            //noinspection unchecked
            return (T) new RequestAcceptedViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(AddDriverRatingViewModel.class)) {
            //noinspection unchecked
            return (T) new AddDriverRatingViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(AddDriverTipViewModel.class)) {
            //noinspection unchecked
            return (T) new AddDriverTipViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(TripHistoryViewModel.class)) {
            //noinspection unchecked
            return (T) new TripHistoryViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(TipThemMoreViewModel.class)) {
            //noinspection unchecked
            return (T) new TipThemMoreViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(AccountSettingsViewModel.class)) {
            //noinspection unchecked
            return (T) new AccountSettingsViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(EditNameViewModel.class)) {
            //noinspection unchecked
            return (T) new EditNameViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(EditPhoneViewModel.class)) {
            //noinspection unchecked
            return (T) new EditPhoneViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(EditEmailViewModel.class)) {
            //noinspection unchecked
            return (T) new EditEmailViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(UpdatePhoneViewModel.class)) {
            //noinspection unchecked
            return (T) new UpdatePhoneViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(SavedPlacesViewModel.class)) {
            //noinspection unchecked
            return (T) new SavedPlacesViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(PromoCodeViewModel.class)) {
            //noinspection unchecked
            return (T) new PromoCodeViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(AddPromoCodeViewModel.class)) {
            //noinspection unchecked
            return (T) new AddPromoCodeViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(AddHomePlaceViewModel.class)) {
            //noinspection unchecked
            return (T) new AddHomePlaceViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(AddWorkPlaceViewModel.class)) {
            //noinspection unchecked
            return (T) new AddWorkPlaceViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(ChangePaymentProfileViewModel.class)) {
            //noinspection unchecked
            return (T) new ChangePaymentProfileViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(AddBusinessProfileViewModel.class)) {
            //noinspection unchecked
            return (T) new AddBusinessProfileViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(FreeRidesViewModel.class)) {
            //noinspection unchecked
            return (T) new FreeRidesViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(TripFareViewModel.class)) {
            //noinspection unchecked
            return (T) new TripFareViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(TripReceiptViewModel.class)) {
            //noinspection unchecked
            return (T) new TripReceiptViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(TripInquiryViewModel.class)) {
            //noinspection unchecked
            return (T) new TripInquiryViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(DriverProfileViewModel.class)) {
            //noinspection unchecked
            return (T) new DriverProfileViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(HelpViewModel.class)) {
            //noinspection unchecked
            return (T) new HelpViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(GeneralInquiryFormViewModel.class)) {
            //noinspection unchecked
            return (T) new GeneralInquiryFormViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(NewBusinessProfileViewModel.class)) {
            //noinspection unchecked
            return (T) new NewBusinessProfileViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(PaymentViewModel.class)) {
            //noinspection unchecked
            return (T) new PaymentViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(AddPaymentMethodViewModel.class)) {
            //noinspection unchecked
            return (T) new AddPaymentMethodViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(PersonalProfileViewModel.class)) {
            //noinspection unchecked
            return (T) new PersonalProfileViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(EditBusinessProfileViewModel.class)) {
            //noinspection unchecked
            return (T) new EditBusinessProfileViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(EditCardViewModel.class)) {
            //noinspection unchecked
            return (T) new EditCardViewModel(schedulerProvider);
        } else if (modelClass.isAssignableFrom(ChatModuleNewViewModel.class)) {
            //noinspection unchecked
            return (T) new ChatModuleNewViewModel(schedulerProvider);
        }else if (modelClass.isAssignableFrom(UpdateRouteViewModel.class)) {
            //noinspection unchecked
            return (T) new UpdateRouteViewModel(schedulerProvider);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}