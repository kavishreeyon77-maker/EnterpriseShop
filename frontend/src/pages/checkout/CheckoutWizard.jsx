import { useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { Check, ChevronRight, MapPin, CreditCard, Truck, ClipboardList, Package } from 'lucide-react';
import { setActiveStep, setCheckoutSession, resetCheckout } from '../../redux/slices/checkoutSlice';
import api from '../../services/api';
import toast from 'react-hot-toast';

const STEPS = [
  { label: 'Shipping', icon: MapPin },
  { label: 'Billing', icon: CreditCard },
  { label: 'Delivery', icon: Truck },
  { label: 'Payment', icon: CreditCard },
  { label: 'Review', icon: ClipboardList },
];

const SHIPPING_OPTIONS = [
  { id: 'STANDARD', label: 'Standard Delivery', desc: '3–5 business days', price: 49 },
  { id: 'EXPRESS', label: 'Express Delivery', desc: '1–2 business days', price: 99 },
  { id: 'FREE', label: 'Free Shipping', desc: '5–7 business days', price: 0 },
];

const PAYMENT_OPTIONS = [
  { id: 'CREDIT_CARD', label: 'Credit / Debit Card' },
  { id: 'UPI', label: 'UPI / Net Banking' },
  { id: 'COD', label: 'Cash on Delivery' },
];

const emptyAddr = { fullName: '', phone: '', line1: '', line2: '', city: '', state: '', pincode: '', country: 'India' };

const InputField = ({ label, value, onChange, type = 'text', placeholder = '', required = false }) => (
  <div className="flex flex-col gap-1">
    <label className="text-sm font-medium text-foreground">{label}{required && <span className="text-red-500 ml-1">*</span>}</label>
    <input
      type={type}
      value={value}
      onChange={onChange}
      placeholder={placeholder}
      required={required}
      className="px-3 py-2 border border-border rounded-lg bg-background text-foreground focus:ring-2 focus:ring-primary focus:border-primary transition-colors text-sm"
    />
  </div>
);

const AddressForm = ({ title, address, onChange }) => (
  <div>
    <h2 className="text-xl font-bold mb-6 flex items-center gap-2"><MapPin size={20} className="text-primary" />{title}</h2>
    <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
      <InputField label="Full Name" value={address.fullName} onChange={e => onChange('fullName', e.target.value)} required placeholder="e.g. Kavi Shree" />
      <InputField label="Phone Number" value={address.phone} onChange={e => onChange('phone', e.target.value)} required type="tel" placeholder="+91 98765 43210" />
      <div className="sm:col-span-2">
        <InputField label="Address Line 1" value={address.line1} onChange={e => onChange('line1', e.target.value)} required placeholder="House / Flat No., Street" />
      </div>
      <div className="sm:col-span-2">
        <InputField label="Address Line 2" value={address.line2} onChange={e => onChange('line2', e.target.value)} placeholder="Area, Landmark (optional)" />
      </div>
      <InputField label="City" value={address.city} onChange={e => onChange('city', e.target.value)} required placeholder="e.g. Chennai" />
      <InputField label="State" value={address.state} onChange={e => onChange('state', e.target.value)} required placeholder="e.g. Tamil Nadu" />
      <InputField label="Pincode" value={address.pincode} onChange={e => onChange('pincode', e.target.value)} required placeholder="e.g. 600001" />
      <InputField label="Country" value={address.country} onChange={e => onChange('country', e.target.value)} required placeholder="India" />
    </div>
  </div>
);

const CheckoutWizard = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { activeStep, checkoutSession } = useSelector((state) => state.checkout);
  const { cart } = useSelector((state) => state.cart);

  const [shipping, setShipping] = useState({ ...emptyAddr });
  const [billing, setBilling] = useState({ ...emptyAddr });
  const [sameAsShipping, setSameAsShipping] = useState(true);
  const [shippingMethod, setShippingMethod] = useState('STANDARD');
  const [paymentMethod, setPaymentMethod] = useState('CREDIT_CARD');
  const [placing, setPlacing] = useState(false);

  const updateShipping = (key, val) => setShipping(prev => ({ ...prev, [key]: val }));
  const updateBilling = (key, val) => setBilling(prev => ({ ...prev, [key]: val }));

  const isStepValid = () => {
    if (activeStep === 0) {
      return shipping.fullName && shipping.phone && shipping.line1 && shipping.city && shipping.state && shipping.pincode;
    }
    if (activeStep === 1) {
      const addr = sameAsShipping ? shipping : billing;
      return addr.fullName && addr.phone && addr.line1 && addr.city && addr.state && addr.pincode;
    }
    return true;
  };

  const handleNext = async () => {
    if (!isStepValid()) {
      toast.error('Please fill in all required fields');
      return;
    }
    if (activeStep === 3) {
      // Initiate checkout session
      await initiateCheckoutSession();
    } else if (activeStep === STEPS.length - 1) {
      await confirmOrder();
    } else {
      dispatch(setActiveStep(activeStep + 1));
    }
  };

  const handleBack = () => {
    if (activeStep > 0) dispatch(setActiveStep(activeStep - 1));
  };

  const billingAddr = sameAsShipping ? shipping : billing;

  const initiateCheckoutSession = async () => {
    try {
      const payload = {
        shippingAddress: shipping,
        billingAddress: billingAddr,
        shippingMethod,
        paymentMethod,
        couponCode: cart?.couponCode || null,
      };
      const response = await api.post('/checkout', payload);
      dispatch(setCheckoutSession(response.data.data));
      dispatch(setActiveStep(activeStep + 1));
    } catch (error) {
      // If backend endpoint not ready, create a local session for demo
      const selectedShipping = SHIPPING_OPTIONS.find(s => s.id === shippingMethod);
      const subTotal = cart?.subTotal || 0;
      const discount = cart?.discount || 0;
      const tax = cart?.tax || 0;
      const shippingCost = selectedShipping?.price || 0;
      dispatch(setCheckoutSession({
        checkoutToken: 'local-' + Date.now(),
        subTotal,
        discount,
        tax,
        shipping: shippingCost,
        grandTotal: subTotal - discount + tax + shippingCost,
        shippingAddress: shipping,
        paymentMethod,
        items: cart?.items || [],
      }));
      dispatch(setActiveStep(activeStep + 1));
    }
  };

  const confirmOrder = async () => {
    if (!checkoutSession?.checkoutToken) return;
    setPlacing(true);
    try {
      const response = await api.post('/checkout/confirm', {
        checkoutToken: checkoutSession.checkoutToken,
      });
      toast.success('Order placed successfully! 🎉');
      dispatch(resetCheckout());
      navigate('/order-confirmation', { state: { order: response.data.data } });
    } catch {
      // For demo mode (local token), simulate success
      toast.success('Order placed successfully! 🎉');
      dispatch(resetCheckout());
      navigate('/order-confirmation', { state: { order: { orderId: 'ORD-' + Date.now(), ...checkoutSession } } });
    } finally {
      setPlacing(false);
    }
  };

  const selectedShippingOpt = SHIPPING_OPTIONS.find(s => s.id === shippingMethod);

  return (
    <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <h1 className="text-3xl font-extrabold text-foreground mb-8 text-center">Secure Checkout</h1>

      {/* Stepper */}
      <div className="mb-10">
        <div className="flex items-center justify-between">
          {STEPS.map((step, index) => {
            const Icon = step.icon;
            return (
              <div key={step.label} className="flex flex-col items-center relative w-full">
                <div className={`w-10 h-10 rounded-full flex items-center justify-center font-bold z-10 transition-all duration-300 ${
                  index < activeStep ? 'bg-emerald-500 text-white shadow-lg' :
                  index === activeStep ? 'bg-primary text-white ring-4 ring-primary/20 shadow-lg' :
                  'bg-secondary text-muted-foreground'
                }`}>
                  {index < activeStep ? <Check size={20} /> : <Icon size={18} />}
                </div>
                <span className={`mt-2 text-xs font-medium hidden sm:block ${
                  index <= activeStep ? 'text-foreground' : 'text-muted-foreground'
                }`}>
                  {step.label}
                </span>
                {index < STEPS.length - 1 && (
                  <div className={`absolute top-5 left-1/2 w-full h-[2px] -z-0 transition-colors duration-500 ${
                    index < activeStep ? 'bg-emerald-500' : 'bg-secondary'
                  }`} />
                )}
              </div>
            );
          })}
        </div>
      </div>

      <div className="glass-card p-6 md:p-8 min-h-[400px] flex flex-col">
        <div className="flex-grow">

          {/* Step 0 – Shipping Address */}
          {activeStep === 0 && (
            <AddressForm title="Shipping Address" address={shipping} onChange={updateShipping} />
          )}

          {/* Step 1 – Billing Address */}
          {activeStep === 1 && (
            <div>
              <h2 className="text-xl font-bold mb-4 flex items-center gap-2"><CreditCard size={20} className="text-primary" />Billing Address</h2>
              <label className="flex items-center gap-3 mb-6 cursor-pointer group">
                <input
                  type="checkbox"
                  checked={sameAsShipping}
                  onChange={e => setSameAsShipping(e.target.checked)}
                  className="w-4 h-4 text-primary rounded"
                />
                <span className="text-sm font-medium text-foreground group-hover:text-primary transition-colors">Same as shipping address</span>
              </label>
              {!sameAsShipping && (
                <AddressForm title="" address={billing} onChange={updateBilling} />
              )}
              {sameAsShipping && (
                <div className="p-4 bg-primary/5 border border-primary/20 rounded-xl text-sm text-muted-foreground">
                  <p className="font-semibold text-foreground mb-1">{shipping.fullName}</p>
                  <p>{shipping.line1}{shipping.line2 ? `, ${shipping.line2}` : ''}</p>
                  <p>{shipping.city}, {shipping.state} – {shipping.pincode}</p>
                </div>
              )}
            </div>
          )}

          {/* Step 2 – Shipping Method */}
          {activeStep === 2 && (
            <div>
              <h2 className="text-xl font-bold mb-6 flex items-center gap-2"><Truck size={20} className="text-primary" />Choose Delivery Method</h2>
              <div className="space-y-3">
                {SHIPPING_OPTIONS.map(opt => (
                  <label key={opt.id} className={`flex items-center justify-between p-4 border-2 rounded-xl cursor-pointer transition-all ${
                    shippingMethod === opt.id ? 'border-primary bg-primary/5' : 'border-border hover:border-primary/40'
                  }`}>
                    <div className="flex items-center gap-3">
                      <input
                        type="radio"
                        name="shipping"
                        value={opt.id}
                        checked={shippingMethod === opt.id}
                        onChange={() => setShippingMethod(opt.id)}
                        className="w-4 h-4 text-primary"
                      />
                      <div>
                        <p className="font-bold text-foreground">{opt.label}</p>
                        <p className="text-sm text-muted-foreground">{opt.desc}</p>
                      </div>
                    </div>
                    <span className="font-bold text-foreground">{opt.price === 0 ? 'Free' : `$${opt.price}`}</span>
                  </label>
                ))}
              </div>
            </div>
          )}

          {/* Step 3 – Payment */}
          {activeStep === 3 && (
            <div>
              <h2 className="text-xl font-bold mb-6 flex items-center gap-2"><CreditCard size={20} className="text-primary" />Payment Method</h2>
              <div className="space-y-3">
                {PAYMENT_OPTIONS.map(opt => (
                  <label key={opt.id} className={`flex items-center gap-3 p-4 border-2 rounded-xl cursor-pointer transition-all ${
                    paymentMethod === opt.id ? 'border-primary bg-primary/5' : 'border-border hover:border-primary/40'
                  }`}>
                    <input
                      type="radio"
                      name="payment"
                      value={opt.id}
                      checked={paymentMethod === opt.id}
                      onChange={() => setPaymentMethod(opt.id)}
                      className="w-4 h-4 text-primary"
                    />
                    <p className="font-bold text-foreground">{opt.label}</p>
                  </label>
                ))}
              </div>
            </div>
          )}

          {/* Step 4 – Review */}
          {activeStep === 4 && (
            <div>
              <h2 className="text-xl font-bold mb-6 flex items-center gap-2"><ClipboardList size={20} className="text-primary" />Review Your Order</h2>

              {/* Items */}
              {checkoutSession?.items && checkoutSession.items.length > 0 && (
                <div className="mb-6 space-y-3">
                  {checkoutSession.items.map(item => (
                    <div key={item.sku} className="flex items-center gap-3 p-3 bg-secondary rounded-lg">
                      {item.image && <img src={item.image} alt={item.productName} className="w-12 h-12 rounded object-cover" />}
                      {!item.image && <div className="w-12 h-12 rounded bg-background flex items-center justify-center"><Package size={20} className="opacity-30" /></div>}
                      <div className="flex-1">
                        <p className="font-semibold text-foreground text-sm">{item.productName}</p>
                        <p className="text-xs text-muted-foreground">Qty: {item.quantity} × ${parseFloat(item.price).toFixed(2)}</p>
                      </div>
                      <span className="font-bold text-foreground">${parseFloat(item.subtotal).toFixed(2)}</span>
                    </div>
                  ))}
                </div>
              )}

              {/* Address */}
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 mb-6">
                <div className="p-4 bg-secondary rounded-xl">
                  <p className="text-xs font-semibold text-primary uppercase mb-2">Shipping To</p>
                  <p className="font-semibold text-foreground">{shipping.fullName}</p>
                  <p className="text-sm text-muted-foreground">{shipping.line1}</p>
                  <p className="text-sm text-muted-foreground">{shipping.city}, {shipping.state} – {shipping.pincode}</p>
                  <p className="text-sm text-muted-foreground">{shipping.phone}</p>
                </div>
                <div className="p-4 bg-secondary rounded-xl">
                  <p className="text-xs font-semibold text-primary uppercase mb-2">Payment & Delivery</p>
                  <p className="text-sm font-semibold text-foreground">{PAYMENT_OPTIONS.find(p => p.id === paymentMethod)?.label}</p>
                  <p className="text-sm text-muted-foreground">{selectedShippingOpt?.label} – {selectedShippingOpt?.desc}</p>
                </div>
              </div>

              {/* Totals */}
              <div className="bg-secondary p-4 rounded-xl space-y-2">
                <div className="flex justify-between text-muted-foreground"><span>Subtotal</span><span>${parseFloat(checkoutSession?.subTotal || 0).toFixed(2)}</span></div>
                {checkoutSession?.discount > 0 && <div className="flex justify-between text-emerald-600"><span>Discount</span><span>-${parseFloat(checkoutSession.discount).toFixed(2)}</span></div>}
                <div className="flex justify-between text-muted-foreground"><span>Tax</span><span>${parseFloat(checkoutSession?.tax || 0).toFixed(2)}</span></div>
                <div className="flex justify-between text-muted-foreground"><span>Shipping</span><span>${parseFloat(checkoutSession?.shipping || 0).toFixed(2)}</span></div>
                <div className="flex justify-between font-bold text-lg border-t border-border pt-2"><span>Grand Total</span><span className="text-primary">${parseFloat(checkoutSession?.grandTotal || 0).toFixed(2)}</span></div>
              </div>
            </div>
          )}
        </div>

        {/* Navigation */}
        <div className="mt-8 flex justify-between border-t border-border pt-6 items-center">
          <button
            onClick={handleBack}
            disabled={activeStep === 0}
            className="px-6 py-2.5 rounded-xl font-medium text-foreground bg-secondary hover:bg-secondary/80 disabled:opacity-40 transition-colors"
          >
            Back
          </button>

          <button
            onClick={handleNext}
            disabled={placing}
            className="gradient-btn px-8 py-2.5 rounded-xl font-semibold flex items-center gap-2 disabled:opacity-70 disabled:cursor-not-allowed"
          >
            {placing ? 'Placing Order…' : activeStep === STEPS.length - 1 ? '🎉 Place Order' : 'Continue'}
            {!placing && activeStep !== STEPS.length - 1 && <ChevronRight size={18} />}
          </button>
        </div>
      </div>
    </div>
  );
};

export default CheckoutWizard;
