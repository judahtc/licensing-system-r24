import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Container } from 'react-bootstrap';
import Navigation from './components/Navigation';
import LicenseList from './pages/LicenseList';
import LicenseForm from './pages/LicenseForm';
import Dashboard from './pages/Dashboard';
import MapView from './pages/MapView';
import Reports from './pages/Reports';

function App() {
  return (
    <Router>
      <div className="App">
        <Navigation />
        <Container className="mt-4">
          <Routes>
            <Route path="/" element={<Dashboard />} />
            <Route path="/licenses" element={<LicenseList />} />
            <Route path="/licenses/new" element={<LicenseForm />} />
            <Route path="/licenses/edit/:id" element={<LicenseForm />} />
            <Route path="/map" element={<MapView />} />
            <Route path="/reports" element={<Reports />} />
          </Routes>
        </Container>
      </div>
    </Router>
  );
}

export default App;