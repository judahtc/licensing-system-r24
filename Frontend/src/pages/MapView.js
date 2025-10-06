import React, { useState, useEffect } from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import { Alert } from 'react-bootstrap';
import { licenseService } from '../services/api';
import L from 'leaflet';

// Fix for default markers in react-leaflet
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png',
  iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
  shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
});

function MapView() {
  const [licenses, setLicenses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchLicenses();
  }, []);

  const fetchLicenses = async () => {
    try {
      const response = await licenseService.getAllLicenses();
      setLicenses(response.data);
      setLoading(false);
    } catch (err) {
      setError('Failed to fetch licenses');
      setLoading(false);
    }
  };

  if (loading) return <div>Loading map...</div>;

  return (
    <div>
      <h2>Geographical Distribution of Licensed Companies</h2>
      
      {error && <Alert variant="danger">{error}</Alert>}

      <div style={{ height: '600px', width: '100%' }}>
        <MapContainer
          center={[-17.8292, 31.0522]} // Zimbabwe coordinates
          zoom={6}
          style={{ height: '100%', width: '100%' }}
        >
          <TileLayer
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          />
          
          {licenses.map((license) => (
            <Marker
              key={license.id}
              position={[license.latitude, license.longitude]}
            >
              <Popup>
                <div>
                  <h6>{license.companyName}</h6>
                  <p><strong>Type:</strong> {license.licenseType}</p>
                  <p><strong>Issue Date:</strong> {license.issueDate}</p>
                  <p><strong>Email:</strong> {license.email}</p>
                  <p><strong>Years Before Expiry:</strong> {license.yearsBeforeExpiry}</p>
                </div>
              </Popup>
            </Marker>
          ))}
        </MapContainer>
      </div>
    </div>
  );
}

export default MapView;