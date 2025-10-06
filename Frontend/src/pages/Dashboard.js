import React, { useState, useEffect } from 'react';
import { Row, Col, Card, Alert, Table } from 'react-bootstrap';
import { Chart as ChartJS, ArcElement, Tooltip, Legend, CategoryScale, LinearScale, BarElement } from 'chart.js';
import { Pie, Bar } from 'react-chartjs-2';
import { licenseService } from '../services/api';

ChartJS.register(ArcElement, Tooltip, Legend, CategoryScale, LinearScale, BarElement);

function Dashboard() {
  const [licenses, setLicenses] = useState([]);
  const [expiringLicenses, setExpiringLicenses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [licensesResponse, expiringResponse] = await Promise.all([
        licenseService.getAllLicenses(),
        licenseService.getLicensesExpiring(90) // Next 90 days
      ]);
      
      setLicenses(licensesResponse.data);
      setExpiringLicenses(expiringResponse.data);
      setLoading(false);
    } catch (err) {
      setError('Failed to fetch dashboard data');
      setLoading(false);
    }
  };

  const getLicenseTypeStats = () => {
    const ctlCount = licenses.filter(l => l.licenseType === 'CTL').length;
    const prslCount = licenses.filter(l => l.licenseType === 'PRSL').length;
    
    return {
      labels: ['Cellular Telecommunication (CTL)', 'Public Radio Station (PRSL)'],
      datasets: [{
        data: [ctlCount, prslCount],
        backgroundColor: ['#FF6384', '#36A2EB'],
        hoverBackgroundColor: ['#FF6384', '#36A2EB']
      }]
    };
  };

  const getYearlyStats = () => {
    const yearCounts = {};
    licenses.forEach(license => {
      const year = new Date(license.issueDate).getFullYear();
      yearCounts[year] = (yearCounts[year] || 0) + 1;
    });

    return {
      labels: Object.keys(yearCounts).sort(),
      datasets: [{
        label: 'Licenses Issued',
        data: Object.values(yearCounts),
        backgroundColor: '#36A2EB',
      }]
    };
  };

  if (loading) return <div>Loading dashboard...</div>;

  return (
    <div>
      <h2>Dashboard</h2>
      
      {error && <Alert variant="danger">{error}</Alert>}

      <Row className="mb-4">
        <Col md={3}>
          <Card className="text-center">
            <Card.Body>
              <Card.Title>Total Licenses</Card.Title>
              <h3 className="text-primary">{licenses.length}</h3>
            </Card.Body>
          </Card>
        </Col>
        <Col md={3}>
          <Card className="text-center">
            <Card.Body>
              <Card.Title>CTL Licenses</Card.Title>
              <h3 className="text-success">{licenses.filter(l => l.licenseType === 'CTL').length}</h3>
            </Card.Body>
          </Card>
        </Col>
        <Col md={3}>
          <Card className="text-center">
            <Card.Body>
              <Card.Title>PRSL Licenses</Card.Title>
              <h3 className="text-info">{licenses.filter(l => l.licenseType === 'PRSL').length}</h3>
            </Card.Body>
          </Card>
        </Col>
        <Col md={3}>
          <Card className="text-center">
            <Card.Body>
              <Card.Title>Expiring Soon</Card.Title>
              <h3 className="text-warning">{expiringLicenses.length}</h3>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <Row className="mb-4">
        <Col md={6}>
          <Card>
            <Card.Header>License Type Distribution</Card.Header>
            <Card.Body>
              <Pie data={getLicenseTypeStats()} />
            </Card.Body>
          </Card>
        </Col>
        <Col md={6}>
          <Card>
            <Card.Header>Licenses Issued by Year</Card.Header>
            <Card.Body>
              <Bar data={getYearlyStats()} />
            </Card.Body>
          </Card>
        </Col>
      </Row>

      {expiringLicenses.length > 0 && (
        <Row>
          <Col>
            <Card>
              <Card.Header>Licenses Expiring Within 90 Days</Card.Header>
              <Card.Body>
                <Table striped bordered hover size="sm">
                  <thead>
                    <tr>
                      <th>Company Name</th>
                      <th>License Type</th>
                      <th>Years Before Expiry</th>
                      <th>Email</th>
                    </tr>
                  </thead>
                  <tbody>
                    {expiringLicenses.map((license) => (
                      <tr key={license.id}>
                        <td>{license.companyName}</td>
                        <td>{license.licenseType}</td>
                        <td>{license.yearsBeforeExpiry}</td>
                        <td>{license.email}</td>
                      </tr>
                    ))}
                  </tbody>
                </Table>
              </Card.Body>
            </Card>
          </Col>
        </Row>
      )}
    </div>
  );
}

export default Dashboard;