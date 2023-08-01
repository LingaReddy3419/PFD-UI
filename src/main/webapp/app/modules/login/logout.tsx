import React, { useLayoutEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { logout } from 'app/shared/reducers/authentication';
import '../home/home.scss';

import { Grid, TextField, Button, Typography, Container } from '@mui/material';

export const Logout = () => {
  const logoutUrl = useAppSelector(state => state.authentication.logoutUrl);
  const dispatch = useAppDispatch();

  useLayoutEffect(() => {
    dispatch(logout());
    if (logoutUrl) {
      window.location.href = logoutUrl;
    }
  });

  const account = useAppSelector(state => state.authentication.account);
  const navigate = useNavigate();
  const inputLabelProps = {
    style: { color: '#ffffff' },
  };

  const inputProps = {
    style: { color: '#ffffff', borderBottom: '1px solid #ffffff' },
  };

  return (
    <div>
      <Grid container style={{ height: '100vh', display: 'flex' }}>
        <Grid item xs={6} style={{ display: 'flex', padding: '20px' }}>
          <Container style={{ width: '20%' }}>
            <img
              src="https://www.lifescienceintegrates.com/wp-content/uploads/2020/09/Sai-logo.jpg"
              alt="Logo"
              style={{ width: '100%', height: '20%' }}
            />
          </Container>
          <Container style={{ display: 'flex', flexDirection: 'column', width: '80%', paddingTop: '30px' }}>
            <Typography variant="h5" style={{ color: '#f1592a', fontWeight: 'bold' }}>
              A Trusted Partner in bringing medicines to life fast.
            </Typography>
            <img
              src="https://www.sailife.com/wp-content/uploads/2022/05/The-Sai-Way-810x540.jpg"
              alt="Image"
              style={{ width: '100%', height: '80%', borderRadius: '100%', marginTop: '20px' }}
            />
          </Container>
        </Grid>
        <Grid
          item
          xs={6}
          style={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
          }}
          className="login-box-container"
        >
          <Container style={{ display: 'flex', flexDirection: 'column', width: '70%' }}>
            {/* <Typography variant="h6" style={{ color: '#ffffff' }}>
                Login Page
              </Typography>
              <TextField
                label="Username"
                fullWidth
                margin="normal"
                InputLabelProps={inputLabelProps}
                InputProps={inputProps}
                value={'admin'}
              />
              <TextField
                label="Password"
                type="password"
                fullWidth
                margin="normal"
                InputLabelProps={inputLabelProps}
                InputProps={inputProps}
                value={'admin'}
              /> */}
            {/* <Button
              variant="contained"
              color="secondary"
              style={{ width: "20%" }}
              onClick={handleLogin}
            >
              Login
            </Button> */}
            <Link
              to="/login"
              style={{
                borderRadius: '5px',
                textDecoration: 'none',
                height: '36px',
                width: '80px',
                backgroundColor: 'green',
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
              }}
            >
              <span style={{ color: '#ffffff', textDecoration: 'none', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                Login
              </span>
            </Link>
          </Container>
        </Grid>
      </Grid>
    </div>
  );
};

export default Logout;
