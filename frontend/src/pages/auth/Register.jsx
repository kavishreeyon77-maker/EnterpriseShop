import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import { Link, useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { Loader2, Mail, Lock, User } from 'lucide-react';
import api from '../../services/api';
import { loginStart, loginSuccess, loginFailure } from '../../redux/slices/authSlice';
import toast from 'react-hot-toast';

const schema = yup.object().shape({
  firstName: yup.string().required('First name is required'),
  lastName: yup.string().required('Last name is required'),
  email: yup.string().email('Invalid email').required('Email is required'),
  password: yup.string().min(6, 'Password must be at least 6 characters').required('Password is required'),
});

const Register = () => {
  const { register, handleSubmit, formState: { errors } } = useForm({
    resolver: yupResolver(schema)
  });
  
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { loading } = useSelector((state) => state.auth);

  const onSubmit = async (data) => {
    dispatch(loginStart());
    try {
      const response = await api.post('/auth/register', data);
      dispatch(loginSuccess(response.data.data));
      toast.success('Registration successful!');
      navigate('/dashboard', { replace: true });
    } catch (error) {
      const message = error.response?.data?.message || 'Registration failed';
      dispatch(loginFailure(message));
      toast.error(message);
    }
  };

  return (
    <div className="min-h-[calc(100vh-16rem)] flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8 glass-card p-8">
        <div>
          <h2 className="mt-6 text-center text-3xl font-extrabold text-foreground">
            Create an account
          </h2>
          <p className="mt-2 text-center text-sm text-muted-foreground">
            Already have an account?{' '}
            <Link to="/login" className="font-medium text-primary hover:text-primary-700 transition-colors">
              Sign in
            </Link>
          </p>
        </div>
        
        <form className="mt-8 space-y-6" onSubmit={handleSubmit(onSubmit)}>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-foreground mb-1">First Name</label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <User className="h-5 w-5 text-muted-foreground" />
                </div>
                <input
                  {...register('firstName')}
                  type="text"
                  className={`w-full pl-10 pr-3 py-2 border rounded-lg bg-background text-foreground focus:ring-2 focus:ring-primary focus:border-primary transition-colors ${errors.firstName ? 'border-destructive' : 'border-border'}`}
                  placeholder="First name"
                />
              </div>
              {errors.firstName && <p className="mt-1 text-sm text-destructive">{errors.firstName.message}</p>}
            </div>

            <div>
              <label className="block text-sm font-medium text-foreground mb-1">Last Name</label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <User className="h-5 w-5 text-muted-foreground" />
                </div>
                <input
                  {...register('lastName')}
                  type="text"
                  className={`w-full pl-10 pr-3 py-2 border rounded-lg bg-background text-foreground focus:ring-2 focus:ring-primary focus:border-primary transition-colors ${errors.lastName ? 'border-destructive' : 'border-border'}`}
                  placeholder="Last name"
                />
              </div>
              {errors.lastName && <p className="mt-1 text-sm text-destructive">{errors.lastName.message}</p>}
            </div>
          </div>

          <div className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-foreground mb-1">Email address</label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <Mail className="h-5 w-5 text-muted-foreground" />
                </div>
                <input
                  {...register('email')}
                  type="email"
                  className={`w-full pl-10 pr-3 py-2 border rounded-lg bg-background text-foreground focus:ring-2 focus:ring-primary focus:border-primary transition-colors ${errors.email ? 'border-destructive' : 'border-border'}`}
                  placeholder="Enter your email"
                />
              </div>
              {errors.email && <p className="mt-1 text-sm text-destructive">{errors.email.message}</p>}
            </div>

            <div>
              <label className="block text-sm font-medium text-foreground mb-1">Password</label>
              <div className="relative">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <Lock className="h-5 w-5 text-muted-foreground" />
                </div>
                <input
                  {...register('password')}
                  type="password"
                  className={`w-full pl-10 pr-3 py-2 border rounded-lg bg-background text-foreground focus:ring-2 focus:ring-primary focus:border-primary transition-colors ${errors.password ? 'border-destructive' : 'border-border'}`}
                  placeholder="Create a password"
                />
              </div>
              {errors.password && <p className="mt-1 text-sm text-destructive">{errors.password.message}</p>}
            </div>
          </div>

          <button
            type="submit"
            disabled={loading}
            className="w-full flex justify-center py-3 px-4 border border-transparent text-sm font-medium rounded-lg text-white gradient-btn focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary disabled:opacity-70 disabled:cursor-not-allowed"
          >
            {loading ? <Loader2 className="animate-spin h-5 w-5" /> : 'Create Account'}
          </button>
        </form>
      </div>
    </div>
  );
};

export default Register;
