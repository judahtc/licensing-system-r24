# Telecommunications License Management System

A comprehensive web-based application for managing telecommunications licenses (CTL and PRSL) with Spring Boot backend and React frontend.

## Features

### Core Functionality
- **License Management**: Create, read, update, delete licenses
- **Two License Types**:
  - Cellular Telecommunication License (CTL): 15-year validity, $800 application fee, $100M license fee
  - Public Radio Station License (PRSL): Variable validity, $350 application fee, $2M license fee
- **Fee Management**: Automatic fee calculation and adjustment capabilities
- **License Comparison**: Compare two licenses for equality
- **Expiry Calculation**: Compute years before license expiration

### Advanced Features
- **Geographical Mapping**: Interactive map showing license locations
- **Dashboard**: Statistics and charts for license distribution
- **Reports**: Generate PDF, Excel, and CSV reports
- **Email Notifications**: Automated expiry notifications
- **Search & Filter**: Find licenses by company name
- **Security**: Role-based access control

## Technology Stack

### Backend
- **Framework**: Spring Boot 3.2.0
- **Database**: PostgreSQL
- **Security**: Spring Security with JWT
- **Email**: Spring Mail
- **Reports**: iText (PDF), Apache POI (Excel), OpenCSV

### Frontend
- **Framework**: React 18
- **UI Library**: React Bootstrap
- **Mapping**: React Leaflet
- **Charts**: Chart.js with react-chartjs-2
- **Routing**: React Router DOM

## Class Diagram

```
License (Abstract)
├── id: Long
├── companyName: String
├── issueDate: LocalDate
├── latitude: Double
├── longitude: Double
├── email: String
├── applicationFee: BigDecimal
├── licenseFee: BigDecimal
├── validityPeriod: Integer
├── getAnnualFrequencyFee(): BigDecimal (abstract)
├── getAnnualUSFContribution(): BigDecimal (abstract)
├── getYearsBeforeExpiry(): int
├── adjustApplicationFee(percentage: double): void
└── equals(obj: Object): boolean

CellularTelecommunicationLicense extends License
├── DEFAULT_APPLICATION_FEE = $800
├── DEFAULT_LICENSE_FEE = $100,000,000
├── DEFAULT_VALIDITY_PERIOD = 15 years
├── ANNUAL_USF_CONTRIBUTION = $3,000
├── getAnnualFrequencyFee(): BigDecimal (returns $0)
└── getAnnualUSFContribution(): BigDecimal (returns $3,000)

PublicRadioStationLicense extends License
├── DEFAULT_APPLICATION_FEE = $350
├── DEFAULT_LICENSE_FEE = $2,000,000
├── ANNUAL_FREQUENCY_FEE = $2,000
├── getAnnualFrequencyFee(): BigDecimal (returns $2,000)
└── getAnnualUSFContribution(): BigDecimal (returns $0)
```

## Setup Instructions

### Prerequisites
- Java 17+
- Node.js 16+
- PostgreSQL 12+
- Maven 3.6+

### Database Setup
1. Create PostgreSQL database:
```sql
CREATE DATABASE license_db;
```

2. Update database credentials in `Backend/src/main/resources/application.yml`

### Backend Setup
```bash
cd Backend
mvn clean install
mvn spring-boot:run
```

### Frontend Setup
```bash
cd Frontend
npm install
npm start
```

## API Endpoints

### License Management
- `GET /api/licenses` - Get all licenses
- `POST /api/licenses` - Create new license
- `GET /api/licenses/{id}` - Get license by ID
- `PUT /api/licenses/{id}` - Update license
- `DELETE /api/licenses/{id}` - Delete license
- `GET /api/licenses/search?companyName={name}` - Search licenses
- `PUT /api/licenses/{id}/adjust-fee` - Adjust application fee
- `GET /api/licenses/compare/{id1}/{id2}` - Compare licenses
- `GET /api/licenses/expiring?days={days}` - Get expiring licenses

### Reports
- `GET /api/licenses/reports/pdf` - Generate PDF report
- `GET /api/licenses/reports/excel` - Generate Excel report
- `GET /api/licenses/reports/csv` - Generate CSV report

## Usage

1. **Access the application**: http://localhost:3000
2. **Dashboard**: View statistics and expiring licenses
3. **License Management**: Add, edit, delete licenses via the Licenses page
4. **Map View**: See geographical distribution of companies
5. **Reports**: Generate and download various report formats
6. **Fee Adjustment**: Adjust application fees by percentage
7. **License Comparison**: Compare two licenses for equality

## Configuration

### Email Settings
Update email configuration in `application.yml`:
```yaml
spring:
  mail:
    host: smtp.gmail.com
    username: your-email@gmail.com
    password: your-app-password
```

### Security
- JWT secret key can be configured via environment variable `JWT_SECRET`
- Default admin user should be created via database seeding

## License Types Details

### CTL (Cellular Telecommunication License)
- Application Fee: $800
- License Fee: $100,000,000
- Validity: 15 years (fixed)
- Annual Frequency Fee: $0
- Annual USF Contribution: $3,000

### PRSL (Public Radio Station License)
- Application Fee: $350
- License Fee: $2,000,000
- Validity: Variable (specified in license)
- Annual Frequency Fee: $2,000
- Annual USF Contribution: $0

## Development Notes

- The system uses inheritance with single table strategy for license types
- Email notifications are scheduled to run weekly
- Maps use OpenStreetMap tiles via Leaflet
- Reports are generated server-side and downloaded as files
- Security is implemented with role-based access (ADMIN/USER roles)