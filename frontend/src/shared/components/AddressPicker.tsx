import React, { useState } from 'react';
import { MapContainer, TileLayer, Marker, useMapEvents } from 'react-leaflet';
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';

// Fix for default marker icon which often breaks in Vite/Webpack
import markerIcon from 'leaflet/dist/images/marker-icon.png';
import markerShadow from 'leaflet/dist/images/marker-shadow.png';

const DefaultIcon = L.icon({
  iconUrl: markerIcon,
  shadowUrl: markerShadow,
  iconSize: [25, 41],
  iconAnchor: [12, 41],
});

L.Marker.prototype.options.icon = DefaultIcon;

interface AddressData {
  latitude: number;
  longitude: number;
  path: string;
  country: string;
  countryCode: string;
  region: string;
  governorate: string;
  city: string;
  suburb: string;
  quarter: string;
  neighborhood: string;
  street: string;
  houseNumber: string;
  postalCode: string;
  displayName: string;
}

interface AddressPickerProps {
  onAddressSelect: (address: AddressData) => void;
  initialLocation?: [number, number];
}

const AddressPicker: React.FC<AddressPickerProps> = ({ 
  onAddressSelect, 
  initialLocation = [36.8065, 10.1815] // Default to Tunis
}) => {
  const [position, setPosition] = useState<L.LatLng>(new L.LatLng(initialLocation[0], initialLocation[1]));

  const reverseGeocode = async (lat: number, lon: number) => {
    try {
      const response = await fetch(
        `https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=${lat}&lon=${lon}`
      );
      const data = await response.json();
      const addr = data.address;

      const city = addr.city || addr.town || addr.village || addr.municipality || '';
      const state = addr.state || '';
      const country = addr.country || '';
      const clean = (s: string) => s ? s.trim().replace(/[^a-zA-Z0-9]/g, '_').replace(/_+/g, '_') : '';
      
      // Build ltree path with encoded coordinates and full hierarchy
      const pathParts = [
        clean(country),
        clean(state),
        clean(city),
        clean(addr.suburb),
        `lat_${lat.toString().replace('.', '_')}`,
        `lon_${lon.toString().replace('.', '_')}`,
        clean(addr.road),
        clean(addr.house_number)
      ].filter(Boolean);

      const path = pathParts.join('.');

      const addressData: AddressData = {
        latitude: lat,
        longitude: lon,
        path: path,
        country: country,
        countryCode: addr.country_code || '',
        region: addr.region || '',
        governorate: state,
        city: city,
        suburb: addr.suburb || '',
        quarter: addr.quarter || '',
        neighborhood: addr.neighborhood || '',
        street: addr.road || '',
        houseNumber: addr.house_number || '',
        postalCode: addr.postcode || '',
        displayName: data.display_name || ''
      };

      onAddressSelect(addressData);
    } catch (error) {
      console.error('Error reverse geocoding:', error);
    }
  };

  const LocationMarker = () => {
    useMapEvents({
      click(e) {
        setPosition(e.latlng);
        reverseGeocode(e.latlng.lat, e.latlng.lng);
      },
    });

    return (
      <Marker 
        position={position} 
        draggable={true}
        eventHandlers={{
          dragend: (e) => {
            const marker = e.target;
            const newPos = marker.getLatLng();
            setPosition(newPos);
            reverseGeocode(newPos.lat, newPos.lng);
          },
        }}
      />
    );
  };

  return (
    <div className="w-full h-[400px] rounded-xl overflow-hidden border-2 border-gray-100 shadow-inner group">
      <MapContainer 
        center={position} 
        zoom={13} 
        scrollWheelZoom={true} 
        style={{ height: '100%', width: '100%' }}
      >
        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        <LocationMarker />
      </MapContainer>
      <div className="p-2 bg-white/80 backdrop-blur-md text-xs text-secondary-500 text-center italic border-t border-gray-100">
        Click or drag the marker to your faculty's precise location
      </div>
    </div>
  );
};

export default AddressPicker;
